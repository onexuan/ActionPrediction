package cn.pointw.actionprediction;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_predict;
    Button btn_dataGatherer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findById();
        init();
    }

    private void findById(){
        btn_predict = (Button) findViewById(R.id.btn_predict);
        btn_dataGatherer = (Button) findViewById(R.id.btn_dataGatherer);
    }

    private void init(){
        btn_predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PredictActivity.class));
            }
        });
        btn_dataGatherer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DataGathererActivity.class));
            }
        });
    }
}
