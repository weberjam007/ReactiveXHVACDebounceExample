package weberjam.reactivexhvacdebounceexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Make scrollable text views for raw stream of commands going out */
        TextView textViewLog = findViewById(R.id.textViewLog);
        textViewLog.setMovementMethod(new ScrollingMovementMethod());

        /* Emit items on button press */
        Button buttonDecrease = findViewById(R.id.buttonDecrease);
        Button buttonIncrease = findViewById(R.id.buttonIncrease);
        PublishSubject<Integer> psDecrease = PublishSubject.create();
        PublishSubject<Integer> psIncrease = PublishSubject.create();
        buttonDecrease.setOnClickListener(v -> psDecrease.onNext((-1)));
        buttonIncrease.setOnClickListener(v -> psIncrease.onNext((1)));

        /* Create observable to emit raise and lower requests as the buttons are pressed*/
        Observable.merge(psDecrease, psIncrease).subscribe(i -> adjustTemperature(i));

        /* Create observable that simulates debounced commands sent to the system */
        TextView textTargetTemperature = findViewById(R.id.textTargetTemperature);
        Observable.merge(psDecrease, psIncrease).debounce(2, TimeUnit.SECONDS)
                .subscribe(s -> logCommand(("Send command: " + textTargetTemperature.getText())));

    }

    /* Helper functions follow. No time spent here. PCO code, you know. :-D */

    private void logCommand(String s) {
        TextView textViewLog = findViewById(R.id.textViewLog);
        s = textViewLog.getText() + System.lineSeparator() + LocalTime.now() + ": " + s;
        textViewLog.setText(s);
        scrollTextViewToEnd(textViewLog);
    }

    private void scrollTextViewToEnd(TextView textViewLog) {
        /* Scroll TextView to the bottom.
            From: https://stackoverflow.com/questions/3506696/auto-scrolling-textview-in-android-to-bring-text-into-view */
        Integer scrollAmount = textViewLog.getLayout().getLineTop(textViewLog.getLineCount()) - textViewLog.getHeight() - 1;
        if (scrollAmount > 0) textViewLog.scrollTo(0, scrollAmount);
        else textViewLog.scrollTo(0, 0);
    }

    private void adjustTemperature(Integer i) {
        logCommand(("User input: " + i.toString()));

        /* Ugly string manipulation. This is the pinical of POC code */
        TextView textTargetTemperature = findViewById(R.id.textTargetTemperature);
        String s = textTargetTemperature.getText().toString();
        Integer currentTargetTemperature = Integer.valueOf(s.substring(0, s.length() - 1)) + i;
        s = currentTargetTemperature.toString() + s.substring(s.length() - 1);
        textTargetTemperature.setText(s);
    }
}
