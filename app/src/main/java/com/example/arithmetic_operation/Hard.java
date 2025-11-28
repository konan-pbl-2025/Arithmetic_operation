package com.example.arithmetic_operation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Hard extends AppCompatActivity {

    private TextView textView;
    private TextView total,total_count,timer;
    private Button kakutei;
    private Button[] numButtons, opButtons;

    private String left = "", op = "", right = "", lastType = "";
    private Button leftBtn, rightBtn;
    private boolean firstDone = false, okMode = false, lastNumPressed = false;
    private int count = 0;

    // ---- タイマー関連 ----ここから追加
    private Handler timerHandler = new Handler();
    private long startTime = 0L;
    private boolean timerRunning = false;
    private Button reset;//これ追加
    private String[] resetVals = new String[5];//これ追加
    private boolean isNewGame = true;

    @Override
    protected void onResume() {
        super.onResume();

        if (isNewGame) {
            resetTimer(); // 表示と時間をリセット
            startTimer();
            isNewGame = false;
        }
    }


    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!timerRunning) return;

            long millis = System.currentTimeMillis() - startTime;
            int minutes = (int) (millis / 60000);
            int seconds = (int) ((millis / 1000) % 60);
            int centiseconds = (int) ((millis % 1000) / 10);

            String time = String.format("%02d:%02d:%02d", minutes, seconds, centiseconds);
            timer.setText(time);

            timerHandler.postDelayed(this, 10);
        }
    };

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timerRunning = true;
        timerHandler.post(timerRunnable);
    }

    private void stopTimer() {
        timerRunning = false;
    }

    private void resetTimer() {
        timerRunning = false;
        timer.setText("00:00:00");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard);

        textView = findViewById(R.id.textView1);
        kakutei = findViewById(R.id.kakutei);
        total = findViewById(R.id.textView2);//これ追加　問題の答え
        total_count = findViewById(R.id.problem);//これも　カウント用
        timer = findViewById(R.id.time); // ← タイマー表示TextView
        reset = findViewById(R.id.reset);

        numButtons = new Button[]{
                findViewById(R.id.num1), findViewById(R.id.num2),
                findViewById(R.id.num3), findViewById(R.id.num4),
                findViewById(R.id.num5)
        };
        opButtons = new Button[]{
                findViewById(R.id.plus1), findViewById(R.id.plus2),
                findViewById(R.id.plus3), findViewById(R.id.plus4)
        };

        Random rand = new Random();
        total_count.setText(count + "/3問正解");//これ追加
        //ここから
        int[] initVals=Creating_Question.createHard();
        total.setText(String.valueOf(initVals[5]));//これで追加
        int j=0;
        for (Button b : numButtons) {
            b.setText(String.valueOf(initVals[j]));
            j++;
        }
        //reset押したときに個の値がいる　ここから追加
        for (int i = 0; i < numButtons.length; i++) {
            resetVals[i] = numButtons[i].getText().toString(); // ← 初期値保存
        }
        reset.setOnClickListener(v->resetAll());
        //ここまで追加
        String[] ops = {"+", "-", "×", "÷"};
        for (int i = 0; i < numButtons.length; i++)
            numButtons[i].setText(String.valueOf(initVals[i]));
        for (int i = 0; i < opButtons.length; i++)
            opButtons[i].setText(ops[i]);

        for (Button b : numButtons) b.setOnClickListener(v -> onNum(b));
        for (Button b : opButtons) b.setOnClickListener(v -> onOp(b));
        kakutei.setOnClickListener(v -> onKakutei());

        updateStates();
    }

    private void onNum(Button b) {
        String val = b.getText().toString();
        if (val.isEmpty()) return;

        if (okMode) {
            lastNumPressed = true;
            left=val;
            textView.setText(val);
            updateStates();
            return;
        }

        if (!firstDone) {
            left = val;
            leftBtn = b;
            textView.setText(left);
            firstDone = true;
            lastType = "num";
        } else if (!op.isEmpty()) {
            if (b == leftBtn) return;
            right = val;
            rightBtn = b;
            textView.setText(left + op + right);
            lastType = "num";
        } else {
            left = val;
            leftBtn = b;
            textView.setText(left);
        }
        updateStates();
    }

    private void onOp(Button b) {
        if (okMode || !firstDone || !right.isEmpty()) return;
        op = b.getText().toString();
        textView.setText(left + op);
        lastType = "op";
        updateStates();
    }

    private void onKakutei() {
        if (okMode) {
            if (!lastNumPressed) return;
            try {
                BigDecimal l = new BigDecimal(left);
                BigDecimal target = new BigDecimal(total.getText().toString()); // 正しい合計値

                if (l.compareTo(target) == 0) {
                    count++;
                }

                total_count.setText(count + "/3問正解");

                if (count == 3) {
                    count = 0;
                    stopTimer();   // タイマー停止
                    Intent intent = new Intent(Hard.this, result.class);//resultActivityはリザルト用に変更
                    String timeValue = timer.getText().toString();  // 例："00:23:45"
                    intent.putExtra("TIME_VALUE", timeValue);
                    String mode="3";
                    intent.putExtra("MODE",mode);
                    isNewGame=true;
                    startActivity(intent);//これらはリザルト画面に行くときにする*/
                    onRestart();
                    return;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            resetNums();
            kakutei.setText("=");
            okMode = lastNumPressed = false;
            textView.setText("");
            updateStates();
            return;
        }

        if (left.isEmpty() || op.isEmpty() || right.isEmpty()) return;

        String exp = left + op + right;
        String resultText;
        /*ここから*/
        try {
            BigDecimal l = new BigDecimal(left);
            BigDecimal r = new BigDecimal(right);
            BigDecimal result;

            switch (op) {
                case "+":
                    result = l.add(r);
                    break;
                case "-":
                    result = l.subtract(r);
                    break;
                case "×":
                    result = l.multiply(r);
                    break;
                case "÷":
                    if (r.compareTo(BigDecimal.ZERO) == 0) {
                        textView.setText(exp + " = エラー");
                        op = "";          // 演算子リセット
                        right = "";       // 右辺クリア
                        rightBtn = null;  // ボタン選択解除
                        lastType = "";    // 状態リセット
                        updateStates();
                        return;
                    }
                    result = l.divide(r, 6, RoundingMode.HALF_UP); // 小数6桁まで正確に割り算
                    break;
                default:
                    return;
            }
            // 小数点以下を適切に丸め
            result = result.stripTrailingZeros();

            // 整数なら小数点なしで表示
            resultText = result.scale() <= 0 ? result.toPlainString()
                    : result.setScale(Math.min(result.scale(), 3), RoundingMode.HALF_UP).toPlainString();

            textView.setText(exp + " = " + resultText);

            leftBtn.setText(resultText);
            rightBtn.setText("");
            rightBtn.setEnabled(false);
            rightBtn.setAlpha(0.4f);

        } catch (Exception e) {
            textView.setText("エラー");
            return;
        }
//ここまで
        left = right = op = lastType = "";
        leftBtn = rightBtn = null;
        firstDone = false;

        if (remainingButtons() == 1) {
            kakutei.setText("OK");
            okMode = true;
            lastNumPressed = false;
        }
        updateStates();
    }

    private void updateStates() {
        if (okMode) {
            for (Button b : numButtons) {
                boolean empty = b.getText().toString().isEmpty();
                b.setEnabled(!empty);
                b.setAlpha(empty ? 0.4f : 1f);
            }
            for (Button b : opButtons) disable(b);
            kakutei.setEnabled(lastNumPressed);
            kakutei.setAlpha(lastNumPressed ? 1f : 0.4f);
            return;
        }

        for (Button b : numButtons) {
            boolean disable = (leftBtn != null && b == leftBtn && !op.isEmpty())
                    || b.getText().toString().isEmpty();
            b.setEnabled(!disable);
            b.setAlpha(!disable ? 1f : 0.4f);
        }
        for (Button b : opButtons)
            enable(b, firstDone && right.isEmpty());

        boolean canConfirm = !left.isEmpty() && !op.isEmpty() && !right.isEmpty();
        kakutei.setEnabled(canConfirm);
        kakutei.setAlpha(canConfirm ? 1f : 0.4f);
    }

    private void resetNums() {
        //ここから
        int[] resetint=Creating_Question.createHard();
        int k=0;
        for (Button b : numButtons) {
            b.setText(String.valueOf(resetint[k]));
            k++;
            b.setEnabled(true);
            b.setAlpha(1f);
        }
        for (int i = 0; i < numButtons.length; i++) {
            resetVals[i] = numButtons[i].getText().toString(); // ← 初期値保存
        }
        total.setText(String.valueOf(resetint[5]));
    }

    private int remainingButtons() {
        int c = 0;
        for (Button b : numButtons)
            if (!b.getText().toString().isEmpty()) c++;
        return c;
    }

    private void disable(Button b) { b.setEnabled(false); b.setAlpha(0.4f); }
    private void enable(Button b, boolean e) { b.setEnabled(e); b.setAlpha(e ? 1f : 0.4f); }
    private void resetAll() {
        left = "";
        op = "";
        right = "";
        textView.setText("");
        firstDone=false;
        okMode=false;
        lastNumPressed=false;
        leftBtn=null;
        rightBtn=null;
        for (int i = 0; i < numButtons.length; i++) {
            numButtons[i].setEnabled(true);
            numButtons[i].setAlpha(1f);
            numButtons[i].setText(String.valueOf(resetVals[i])); // ←初期値に戻す！
        }

        rightBtn = null;
    }

}
