package com.tuya.smart.android.demo.camera.bean;

import java.util.List;

/**
 * Created by lee on 2017/5/23.
 */

public class CameraInfoBean {
    private String password;
    private String p2pId;
    private P2pConfig p2pConfig;
    private AudioAttributes audioAttributes;  // -----对讲模式  1---单工  2---双工 ; -----硬件能力  1---拾音器  2---扬声器

    public CameraInfoBean() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }

    public P2pConfig getP2pConfig() {
        return p2pConfig;
    }

    public AudioAttributes getAudioAttributes() {
        return audioAttributes;
    }

    public void setAudioAttributes(AudioAttributes audioAttributes) {
        this.audioAttributes = audioAttributes;
    }

    public void setP2pConfig(P2pConfig p2pConfig) {
        this.p2pConfig = p2pConfig;
    }

    public static  class P2pConfig{
        String initStr;
        String p2pKey;

        public String getInitStr() {
            return initStr;
        }

        public void setInitStr(String initStr) {
            this.initStr = initStr;
        }

        public String getP2pKey() {
            return p2pKey;
        }

        public void setP2pKey(String p2pKey) {
            this.p2pKey = p2pKey;
        }
    }


    public static class AudioAttributes{
        List<Integer> callMode;
        List<Integer> hardwareCapability;

        public List<Integer> getCallMode() {
            return callMode;
        }

        public void setCallMode(List<Integer> callMode) {
            this.callMode = callMode;
        }

        public List<Integer> getHardwareCapability() {
            return hardwareCapability;
        }

        public void setHardwareCapability(List<Integer> hardwareCapability) {
            this.hardwareCapability = hardwareCapability;
        }
    }

    @Override
    public String toString() {
        return "CameraInfoBean{" +
                "password='" + password + '\'' +
                ", p2pId='" + p2pId + '\'' +
                ", p2pConfig=" + p2pConfig +
                ", audioAttributes=" + audioAttributes +
                '}';
    }
}
