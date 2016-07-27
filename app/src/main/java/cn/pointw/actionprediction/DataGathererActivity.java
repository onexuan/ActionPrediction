package cn.pointw.actionprediction;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pointw.actionprediction.model.Item;
import cn.pointw.actionprediction.model.JsonItem;
import cn.pointw.actionprediction.util.Features;

public class DataGathererActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private MySensorListener sensorListener;
    private List<Double> list;
    private String[] actString;
    private String[] positionString;
    private boolean registered;

    private TextView tv_acc;
    private TextView tv_count;
    private Button btn_act;
    private Button btn_position;
    private Button btn_start;
    private Button btn_stop;

    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_gatherer);
        findById();
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (registered) {
            sensorManager.unregisterListener(sensorListener);
            list.clear();
            registered = false;
        }
    }

    private void findById(){
        tv_acc = (TextView) findViewById(R.id.tv_acc);
        tv_count = (TextView) findViewById(R.id.tv_count);
        btn_act = (Button) findViewById(R.id.btn_act);
        btn_position = (Button) findViewById(R.id.btn_position);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
    }

    private void init(){
        actString = new String[]{"Walking", "Running", "Still"};
        positionString = new String[]{"Hand Swing", "Hand Fixed"};
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/Data.txt";
        file = new File(path);
        if (!file.exists()){
            try {
                file.createNewFile();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        list = new ArrayList<>();
        btn_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlertDialog
                        .Builder(DataGathererActivity.this)
                        .setTitle("请选择动作")
                        .setSingleChoiceItems(actString, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        btn_act.setText(actString[i]);
                                        dialogInterface.dismiss();
                                    }
                                }).show();
            }
        });
        btn_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new AlertDialog
                        .Builder(DataGathererActivity.this)
                        .setTitle("请选择姿势")
                        .setSingleChoiceItems(positionString, 0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        btn_position.setText(positionString[i]);
                                        dialogInterface.dismiss();
                                    }
                                }).show();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registered){
                    sensorManager.registerListener(sensorListener,
                            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            1000*1000/32);
                    registered = true;
                }
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registered) {
                    sensorManager.unregisterListener(sensorListener);
                    list.clear();
                    registered = false;
                }
            }
        });
    }

    public class MySensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//                Log.e("sensor", "sensor data changed");
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double value = Math.sqrt(x*x+y*y+z*z);
                list.add(value);
                tv_count.setText(Integer.toString(list.size()));
                tv_acc.setText(Double.toString(value));
                if (list.size() == 128){
                    double[] doubles = new double[128];
                    for (int i = 0; i < 128; i++){
                        doubles[i] = list.get(i);
                    }
                    list.clear();
//                    Item item = new Item();
//                    item.setAct(btn_act.getText().toString());
//                    item.setPosition(btn_position.getText().toString());
//                    item.setT_min(Features.minimum(doubles));
//                    item.setT_max(Features.maximum(doubles));
//                    item.setT_mcr(Features.meanCrossingsRate(doubles));
//                    item.setT_sttdev(Features.standardDeviation(doubles));
//                    item.setT_mean(Features.mean(doubles));
//                    item.setT_rms(Features.rms(doubles));
//                    item.setT_iqr(Features.iqr(doubles));
//                    item.setT_mad(Features.mad(doubles));
//                    item.setT_variance(Features.variance(doubles));
                    Gson gson = new Gson();
                    JsonItem jsonItem = new JsonItem();
                    jsonItem.setAct(btn_act.getText().toString());
                    jsonItem.setPosition(btn_position.getText().toString());
                    jsonItem.setAcc(gson.toJson(doubles));
                    writeFile(gson.toJson(jsonItem));
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
    public void writeFile(String writeStr){
        writeStr = ","+writeStr;
        try{
            FileOutputStream fout = new FileOutputStream(file, true);

            byte [] bytes = writeStr.getBytes();

            fout.write(bytes);

            fout.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

}
