package cn.pointw.actionprediction.model;

import java.util.DoubleSummaryStatistics;

import cn.pointw.actionprediction.util.Constant;

/**
 * Created by outen on 16/7/27.
 */
public class Item2 {
    private String act;
    private String position;
    private Double t_energy;
    private Double t_entropy;
    private Double t_iqr;
    private Double t_mad;
    private Double t_max;
    private Double t_mcr;
    private Double t_mean;
    private Double t_min;
    private Double t_rms;
    private Double t_spp;
    private Double t_stddev;
    private Double t_variance;

    public Item2(String act, String position, Double t_energy, Double t_entropy, Double t_iqr, Double t_mad,
                 Double t_max, Double t_mcr, Double t_mean, Double t_min, Double t_rms, Double t_spp, Double t_stddev, Double t_variance) {
        this.act = act;
        this.position = position;
        this.t_energy = t_energy;
        this.t_entropy = t_entropy;
        this.t_iqr = t_iqr;
        this.t_mad = t_mad;
        this.t_max = t_max;
        this.t_mcr = t_mcr;
        this.t_mean = t_mean;
        this.t_min = t_min;
        this.t_rms = t_rms;
        this.t_spp = t_spp;
        this.t_stddev = t_stddev;
        this.t_variance = t_variance;
    }

    public Item2(){
    }

    public String[] toStringArr(){
        String[] str = new String[12];
        str[0] = Constant.FUN_101_MINIMUM_CODE+":"+Double.toString(t_min);
        str[1] = Constant.FUN_102_MAXIMUM_CODE+":"+Double.toString(t_max);
        str[2] = Constant.FUN_103_VARIANCE_CODE+":"+Double.toString(t_variance);
        str[3] = Constant.FUN_104_MEANCROSSINGSRATE_CODE+":"+Double.toString(t_mcr);
        str[4] = Constant.FUN_105_STANDARDDEVIATION_CODE+":"+Double.toString(t_stddev);
        str[5] = Constant.FUN_106_MEAN_CODE+":"+Double.toString(t_mean);
        str[6] = Constant.FUN_112_RMS_CODE+":"+Double.toString(t_rms);
        str[7] = Constant.FUN_114_IQR_CODE+":"+Double.toString(t_iqr);
        str[8] = Constant.FUN_115_MAD_CODE+":"+Double.toString(t_mad);

        str[9] = Constant.FUN_201_SPP_CODE+":"+Double.toString(t_spp);
        str[10] = Constant.FUN_202_ENERGY_CODE+":"+Double.toString(t_energy);
        str[11] = Constant.FUN_203_ENTROPY_CODE+":"+Double.toString(t_entropy);
        return str;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getT_min() {
        return t_min;
    }

    public void setT_min(Double t_min) {
        this.t_min = t_min;
    }

    public Double getT_max() {
        return t_max;
    }

    public void setT_max(Double t_max) {
        this.t_max = t_max;
    }

    public Double getT_mcr() {
        return t_mcr;
    }

    public void setT_mcr(Double t_mcr) {
        this.t_mcr = t_mcr;
    }

    public Double getT_sttdev() {
        return t_stddev;
    }

    public void setT_sttdev(Double t_sttdev) {
        this.t_stddev = t_sttdev;
    }

    public Double getT_mean() {
        return t_mean;
    }

    public void setT_mean(Double t_mean) {
        this.t_mean = t_mean;
    }

    public Double getT_rms() {
        return t_rms;
    }

    public void setT_rms(Double t_rms) {
        this.t_rms = t_rms;
    }

    public Double getT_iqr() {
        return t_iqr;
    }

    public void setT_iqr(Double t_iqr) {
        this.t_iqr = t_iqr;
    }

    public Double getT_mad() {
        return t_mad;
    }

    public void setT_mad(Double t_mad) {
        this.t_mad = t_mad;
    }

    public Double getT_variance() {
        return t_variance;
    }

    public void setT_variance(Double t_variance) {
        this.t_variance = t_variance;
    }

    public Double getT_spp() {
        return t_spp;
    }

    public void setT_spp(Double t_spp) {
        this.t_spp = t_spp;
    }

    public Double getT_entropy() {
        return t_entropy;
    }

    public void setT_entropy(Double t_entropy) {
        this.t_entropy = t_entropy;
    }

    public Double getT_energy() {
        return t_energy;
    }

    public void setT_energy(Double t_energy) {
        this.t_energy = t_energy;
    }
}
