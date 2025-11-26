package com.example.arithmetic_operation;
import java.util.Random;

public class Creating_Question {

    // 共通部分：ランダムな数字を生成するメソッド（イージーモード）
    private static int[] generateEasyRandomNumbers() {
        int[] question = new int[5];
        Random rand = new Random();

        for (int i = 0; i < 5; i++) {
            question[i] = rand.nextInt(9) + 1;  // 1から9までのランダムな整数を生成
        }

        return question;
    }

    // 共通部分：ランダムな数字を生成するメソッド（ノーマルモード）
    private static int[] generateNormalRandomNumbers() {
        int[] question = new int[5];
        Random rand = new Random();

        for (int i = 0; i < 5; i++) {
            int num;
            do {
                num = rand.nextInt(19) - 9;  // -9から9までのランダムな整数を生成
            } while (num == 0);  // 0は再生成
            question[i] = num;
        }

        return question;
    }

    // ハードモードの計算（割り算を含む）
    private static int[] generateHardRandomNumbers() {
        int[] question = new int[5];
        Random rand = new Random();

        // -9から9までのランダムな数字を5つ生成、0は除外
        for (int i = 0; i < 5; i++) {
            int num;
            do {
                num = rand.nextInt(19) - 9;
            } while (num == 0);  // 0は除外
            question[i] = num;
        }

        return question;
    }

    // イージーモードの問題を生成
    public static int[] createEasy() {
        int[] question = generateEasyRandomNumbers();  // ランダムな数字を取得
        Random rand = new Random();
        int number = rand.nextInt(6) + 1;  // 1から6の間でランダムに選んだ数

        switch (number) {
            case 1:
                question[4] = question[0] - question[1] + question[2] + question[3] - question[4];  // 足し算
                break;
            case 2:
                question[4] = question[0] + question[1] - question[2] + question[3] - question[4];// 引き算
                break;
            case 3:
                question[4] = question[0] * question[1] + question[2] - question[3] + question[4];// 掛け算
                break;
            case 4:
                question[4] = question[0] + question[1] * question[2] - question[3] + question[4];
                break;
            case 5:
                question[4] = question[0] * question[1] - question[2] * question[3] + question[4];// 足し算 + 掛け算
                break;
            case 6:
                question[4] = question[0] + question[1] - question[2] * question[3] + question[4];// 掛け算 + 足し算
                break;
        }

        return question;
    }

    // ノーマルモードの問題を生成
    public static int[] createNormal() {
        int[] question = generateNormalRandomNumbers();
        Random rand = new Random();
        int number = rand.nextInt(6) + 1;

        switch (number) {
            case 1:
                question[4] = question[0] * question[3] + question[1] - question[4] + question[2];
                break;
            case 2:
                question[4] = question[0] - question[1] - question[2] + question[3] * question[4];
                break;
            case 3:
                question[4] = question[1] + question[2] * question[0] - question[4] - question[3];
                break;
            case 4:
                question[4] = question[2] * question[1] + question[3] - question[0] * question[4];
                break;
            case 5:
                question[4] = question[1] * question[0] + question[2] - question[3] + question[4];
                break;
            case 6:
                question[4] = question[0] + question[1] * question[3] - question[2] + question[4];
                break;
        }

        return question;
    }

    // ハードモードの問題を生成（割り算を含む）
    public static int[] createHard() {
        int[] question = new int[5];
        boolean validAnswer = false;
        Random rand = new Random();

        while (!validAnswer) {
            question = generateHardRandomNumbers();  // ランダムな数字を取得
            int number = rand.nextInt(6) + 1;

            switch (number) {
                case 1:
                    if (question[1] != 0 && question[0] % question[1] == 0) {
                        question[4] = question[0] / question[1] + question[2] * question[3] * question[4];  // 割り算 + 掛け算
                        validAnswer = true;
                    }
                    break;
                case 2:
                    if (question[2] != 0 && question[1] % question[2] == 0) {
                        question[4] = question[0] * question[3] + question[4] - question[2] / question[1];  // 掛け算 + 割り算
                        validAnswer = true;
                    }
                    break;
                case 3:
                    if (question[3] != 0 && question[0] % question[3] == 0) {
                        question[4] = question[0] / question[3] + question[2] * question[1] - question[4];  // 割り算 + 掛け算
                        validAnswer = true;
                    }
                    break;
                case 4:
                    if (question[0] != 0 && question[3] % question[0] == 0) {
                        question[4] = question[2] * question[1] - question[4] + question[3] / question[0];  // 掛け算 + 割り算
                        validAnswer = true;
                    }
                    break;
                case 5:
                    if (question[3] != 0 && question[2] % question[3] == 0) {
                        question[4] = question[1] * question[0] - question[2] / question[3] + question[4];  // 掛け算 + 割り算
                        validAnswer = true;
                    }
                    break;
                case 6:
                    if (question[4] != 0 && question[2] % question[4] == 0) {
                        question[4] = question[0] * question[3] + question[1] * question[2] / question[4];  // 足し算 + 割り算
                        validAnswer = true;
                    }
                    break;
            }
        }

        return question;
    }

    // デバッグ用mainメソッド
    public static void main(String[] args) {
        int[] easyQuestion = createEasy();
        System.out.print("イージーモード: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(easyQuestion[i] + " ");
        }
        System.out.println("\n答え: " + easyQuestion[4]);

        int[] normalQuestion = createNormal();
        System.out.print("ノーマルモード: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(normalQuestion[i] + " ");
        }
        System.out.println("\n答え: " + normalQuestion[4]);

        int[] hardQuestion = createHard();
        System.out.print("ハードモード: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(hardQuestion[i] + " ");
        }
        System.out.println("\n答え: " + hardQuestion[4]);
    }
}
