package cn.pointw.actionprediction;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.pointw.actionprediction.model.Item;
import cn.pointw.actionprediction.model.Item2;
import cn.pointw.actionprediction.util.Constant;
import cn.pointw.actionprediction.util.Features;
import cn.pointw.actionprediction.util.Util;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class PredictActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private MySensorListener sensorListener;
    private List<Double> list;
    private TextView tv_predict;
    private TextView tv_acc;
    private TextView tv_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        findById();
        init();
    }

    private void findById() {
        tv_predict = (TextView) findViewById(R.id.tv_predict);
        tv_acc = (TextView) findViewById(R.id.tv_acc);
        tv_count = (TextView) findViewById(R.id.tv_count);
    }

    private void init() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorListener = new MySensorListener();
        sensorManager.registerListener(sensorListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                1000*1000/32);
        list = new ArrayList<>();
    }

    public class MySensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                Log.e("sensor", "sensor data changed");
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                double value = Math.sqrt(x * x + y * y + z * z);
                list.add(value);
                tv_count.setText(Integer.toString(list.size()));
                tv_acc.setText(Double.toString(value));
                if (list.size() == 128) {
                    double[] doubles = new double[128];
                    for (int i = 0; i < 128; i++) {
                        doubles[i] = list.get(i);
                    }
                    double[] fft = Features.fft(doubles);
//                    Item item = new Item();
                    Item2 item = new Item2();
                    item.setT_min(Features.minimum(doubles));
                    item.setT_max(Features.maximum(doubles));
                    item.setT_mcr(Features.meanCrossingsRate(doubles));
                    item.setT_sttdev(Features.standardDeviation(doubles));
                    item.setT_mean(Features.mean(doubles));
                    item.setT_rms(Features.rms(doubles));
                    item.setT_iqr(Features.iqr(doubles));
                    item.setT_mad(Features.mad(doubles));
                    item.setT_variance(Features.variance(doubles));

                    item.setT_energy(Features.energy(fft));
                    item.setT_entropy(Features.entropy(fft));
                    item.setT_spp(Features.spp(fft));
                    tv_predict.setText(predict(item));
                    list.clear();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    private String predict(Item item){
        String[] ruleArr = readRule("range");
        int sum = ruleArr.length-2;
        String[] tempArr = null;
        String[] tempNode = null;
        tempArr = ruleArr[1].split(" ");
        double lower = Double.parseDouble(tempArr[0]);
        double upper = Double.parseDouble(tempArr[1]);
        svm_node[] px = new svm_node[sum];
        svm_node p = null;
        String[] textArr = item.toStringArr();
        for(int i = 0; i < 9; i++){
            p = new svm_node();
            tempArr = ruleArr[i+2].split(" ");
            tempNode = textArr[i].split(":");
            p.index = Integer.parseInt(tempNode[0]);
            p.value = Features.zeroOneLibSvm(lower, upper,
                    Double.parseDouble(tempNode[1]),
                    Double.parseDouble(tempArr[1]),
                    Double.parseDouble(tempArr[2]));
            px[i] = p;
        }
        try {
            svm_model model = svm.svm_load_model(readFileToBufferedReader("model"));
            double code = svm.svm_predict(model, px);
            return Constant.actMapFromCode.get(code);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String predict(Item2 item){
        String[] ruleArr = readRule("range");
        int sum = ruleArr.length-2;
        String[] tempArr = null;
        String[] tempNode = null;
        tempArr = ruleArr[1].split(" ");
        double lower = Double.parseDouble(tempArr[0]);
        double upper = Double.parseDouble(tempArr[1]);
        svm_node[] px = new svm_node[sum];
        svm_node p = null;
        String[] textArr = item.toStringArr();
        for(int i = 0; i < 12; i++){
            p = new svm_node();
            tempArr = ruleArr[i+2].split(" ");
            tempNode = textArr[i].split(":");
            p.index = Integer.parseInt(tempNode[0]);
            p.value = Features.zeroOneLibSvm(lower, upper,
                    Double.parseDouble(tempNode[1]),
                    Double.parseDouble(tempArr[1]),
                    Double.parseDouble(tempArr[2]));
            px[i] = p;
        }
        try {
            svm_model model = svm.svm_load_model(readFileToBufferedReader("model"));
            double code = svm.svm_predict(model, px);
            return Constant.actMapFromCode.get(code);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private  String readFile(String fileName){
//        try {
//            InputStream in = null;
//            switch (fileName){
//                case "range":
//                    in = getResources().openRawResource(R.raw.range);
//                    break;
//                case "model":
//                    in = getResources().openRawResource(R.raw.model);
//            }
//            int length = in.available();
//            byte[] buffer = new byte[length];
//            in.read(buffer);
//            return buffer.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String str = "";
        try {
            switch (fileName){
                case "range":
                    is = getResources().openRawResource(R.raw.range);
                    break;
                case "model":
                    is = getResources().openRawResource(R.raw.model);
            }
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line ="";							// InputStreamReader的对象
            while ((line = br.readLine()) != null) {
                str += line + Util.getChangeRow();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    private BufferedReader readFileToBufferedReader(String fileName){
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            switch (fileName){
                case "range":
                    is = getResources().openRawResource(R.raw.range);
                    break;
                case "model":
                    is = getResources().openRawResource(R.raw.model);
            }
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return br;
    }

    private String[] readRule(String rulePath){
        String ruleStr = readFile(rulePath);
        String[] ruleArr = ruleStr.split(Util.getChangeRow());
        return ruleArr;
    }
}
