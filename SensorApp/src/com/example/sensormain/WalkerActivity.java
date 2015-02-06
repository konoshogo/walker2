package com.example.sensormain;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class WalkerActivity extends Activity {

    private static final int SHAKE_THRESHOLD = 5;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private TextView mTvShakeCounter;
    private int mCount = 0;
    private long mPrevTime;
    private float mPrevX;
    private float mPrevY;
    private float mPrevZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker);
        // �V�F�C�N�J�E���^�[�̏���
        mTvShakeCounter = (TextView) findViewById(R.id.ShakeCount);

        // �X�C�b�`�E�B�W�F�b�g�̏���
        Switch shakeSwitch = (Switch) findViewById(R.id.AccelerationSwitch);
        shakeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // �Z���T�[ON
                    // �����x�Z���T�[���擾
                    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
                    if (sensors.size() > 0) {
                        // �����x�Z���T�[���Z���T�[�}�l�[�W���[�ɓo�^
                        Sensor s = sensors.get(0);
                        mSensorManager.registerListener(mSensorEventListener, s,
                                SensorManager.SENSOR_DELAY_UI);
                    }
                } else { // �Z���T�[OFF
                    // �����x�Z���T�[���Z���T�[�}�l�[�W���[����폜
                    mSensorManager.unregisterListener(mSensorEventListener);
                    mTvShakeCounter.setText(getString(R.string.shake_count, 0));
                }
            }
        });

        // �Z���T�[�}�l�[�W���[���擾
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // �Z���T�[�C�x���g���X�i�[�̐ݒ�
        mSensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                // �ȑO�Z���T�[���ғ��������Ԃƌ��݂̎��Ԃ̍������߁A
                // 400ms�ȏ�o�߂��Ă���Ώ�������(�f�B���C��݂���)
                long nowTime = System.currentTimeMillis();
                long diffTime = nowTime - mPrevTime;
                if (diffTime > 400) {
                    // X,Y,Z���̉����x���擾
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    // �ȑO�擾���������x�̍������߃V�F�C�N�l�ɂ���
                    float shakeValue = Math.abs(x + y + z - mPrevX - mPrevY - mPrevZ);

                    // �V�F�C�N�l���������l�ȏ�ł���΃V�F�C�N����
                    if (shakeValue > SHAKE_THRESHOLD) {
                        // ��ʂ̃J�E���g�𑝂₷
                        mCount++;
                        mTvShakeCounter.setText(getString(R.string.shake_count, mCount));
                       // Log.i("W",String.format("%d", mCount));
                    }

                    // �擾���ԂƉ����x��ۑ�
                    mPrevTime = nowTime;
                    mPrevX = x;
                    mPrevY = y;
                    mPrevZ = z;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
