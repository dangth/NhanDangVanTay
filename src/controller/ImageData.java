/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Pixel;

/**
 *
 * @author PHANHAI
 */
public class ImageData {

    private Pixel[][] data;
    private int Mean;
    private int Variance;
    private int square;
    private int height;
    private int width;

    public ImageData(Pixel[][] data) {
        this.data = data;
        height = data.length;
        width = data[0].length;
        Mean = 50;
        Variance = 200;
        square = 17;
    }

    public void setMean(int Mean) {
        this.Mean = Mean;
    }

    public void setVariance(int Variance) {
        this.Variance = Variance;
    }

    public void setSquare(int square) {
        this.square = square;
    }

    public void setData(Pixel[][] data) {
        this.data = data;
    }

    /**
     * Tạo ảnh chuẩn hóa
     * @return
     */
    public Pixel[][] getNormalizedGrayImage() {
        float[][] grays = new float[height][width];

        float mean = 0f;        // Độ xám trung bình
        // chuyển sang ảnh xám
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // Gray = 0.299*Red + 0.587*Green + 0.114*Blue
                grays[i][j] = (0.299f * data[i][j].getRed() + 0.587f * data[i][j].getGreen() + 0.114f * data[i][j].getBlue());
                mean += grays[i][j];
            }
        }
        mean /= height * width; // Get mean value
        // Tính phương sai
        float variance = 0f;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                variance += (grays[i][j] - mean) * (grays[i][j] - mean);
            }
        }
        variance /= height * width;
        // Chuẩn hóa ảnh xám
        float[][] mGrays = new float[height][width];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (grays[i][j] > mean) {
                    mGrays[i][j] = (float) (Mean + Math.sqrt(Variance * Math.pow(grays[i][j] - mean, 2) / variance));
                } else {
                    mGrays[i][j] = (float) (Mean - Math.sqrt(Variance * Math.pow(grays[i][j] - mean, 2) / variance));
                }
            }
        }

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                data[i][j] = new Pixel(mGrays[i][j], mGrays[i][j], mGrays[i][j]);
            }
        }
        return data;
    }

    /**
     * Tạo ảnh tăng cường
     * @param square
     * @return
     */
    public Pixel[][] getEnhanceQualityImage() {
        // data =
        getNormalizedGrayImage();
        float[][] direction = getDirectionMatrix();
        // for (int i = square / 2; i < height - (square / 2); i++) {
        for (int i = 0; i < height; ++i) {
            //     for (int j = square / 2; j < width - (square / 2); j++) {
            for (int j = 0; j < width; ++j) {
                float g;
                float x = (float) (i * Math.sin(direction[i][j]) + j * Math.cos(direction[i][j]));
                float y = (float) (-i * Math.cos(direction[i][j]) + j * Math.sin(direction[i][j]));
                g = (float) (Math.exp((-1 / 2) * (x * x + y * y) / 9) * Math.cos(2 * Math.PI * x * 0.2));
                float gray = g * data[i][j].getGray();//data[i][j].getGray();
                if (gray > 255) {
                    data[i][j] = new Pixel(255, 255, 255);
                }
                if (gray < 0) {
                    data[i][j] = new Pixel(0, 0, 0);
                }
            }
        }
        return data;
    }

    /**
     * Tính ma trận định hướng
     * @return 
     */
    public float[][] getDirectionMatrix() {
        float[][] direction = new float[height][width];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                direction[i][j] = 0;
            }
        }

        float Vx, Vy, Vxy;   // Huong cua cac o
        float Gx, Gy;   // Tinh gradien cho o trung tam

        // Duyet cac o vuong
        for (int y = 1 + square / 2; y < height - (square / 2) - 1; y++) {
            for (int x = 1 + square / 2; x < width - (square / 2) - 1; x++) {
                Vx = 0;
                Vy = 0;
                Vxy = 0;
                // Duyet cac o cuc bo de tinh gradian
                for (int i = y - (square / 2); i < y + (square / 2); ++i) {
                    for (int j = x - (square / 2); j < x + (square / 2); ++j) {
                        Gx = (data[i - 1][j - 1].getGray() + 2 * data[i][j - 1].getGray() + data[i + 1][j - 1].getGray())
                                - (data[i - 1][j + 1].getGray() + 2 * data[i][j + 1].getGray() + data[i + 1][j + 1].getGray());
                        Gy = (data[i - 1][j - 1].getGray() + 2 * data[i - 1][j].getGray() + data[i - 1][j + 1].getGray())
                                - (data[i + 1][j - 1].getGray() + 2 * data[i + 1][j].getGray() + data[i + 1][j + 1].getGray());

                        Vxy += Gx * Gy;
                        Vx += Gx * Gx;
                        Vy += Gy * Gy;
                    }
                }
                direction[y][x] = (float) (Math.PI / 2 + 0.5 * Math.atan(2 * Vxy / (Vx - Vy)));
            }
        }
        return direction;
    }

    /**
     * Tạo ảnh nhị phân
     * @return 
     */
    public Pixel[][] getBinaryImage() {
        getNormalizedGrayImage();

        float threShold = getThreshold();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // float gray = (0.299f * data[i][j].getRed() + 0.587f * data[i][j].getGreen() + 0.114f * data[i][j].getBlue());
                float gray = data[i][j].getGray();
                // float gray = grays[i][j];
                if (gray >= threShold) {
                    data[i][j] = new Pixel(255, 255, 255);
                } else {
                    data[i][j] = new Pixel(0, 0, 0);
                }
            }
        }
        return data;
    }

    /**
     * Tính độ xám trung bình để làm mốc nhị phân hóa
     * @return 
     */
    public float getThreshold() {
        // getNormalizedGrayImage();
        float max = 0;
        //
        int gT255 = 0;
        float gM255 = 0;
        int threshold = 131;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (data[i][j].getGray() <= 255) {
                    gT255++;
                    gM255 += data[i][j].getGray();
                }
            }
        }
        gM255 /= gT255;

        for (int x = 0; x <= 255; ++x) {
            int gT = 0; // Tinh tong so diem anh co do xam <= x
            float gM = 0; // Do xam trung binh cua cac diem anh co do xam <= x
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < width; ++j) {
                    if (data[i][j].getGray() <= x) {
                        gT++;
                        gM += data[i][j].getGray();
                    }
                }
            }
            // Tinh do xam trung binh cua cac diem anh co do xam <= x
            gM /= gT;
            float fx = (gT * (gM - gM255) * (gM - gM255)) / (height * width - gT);// - gM255;
            if (fx > max) {
                max = fx;
                threshold = x;
            }
        }
        return threshold;
    }

    /**
     * Kiểm tra điểm biên
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isBorder(int x, int y) {
        if (data[x][y].getGray() == 255) {
            return false;
        }
        if (data[x][y - 1].getGray() == 255 || data[x][y + 1].getGray() == 255 || data[x - 1][y].getGray() == 255 || data[x + 1][y].getGray() == 255) {
            return true;
        }
        return false;
    }

    /**
     * Kiểm tra xem 1 điểm có thể xóa hay không
     * @param x
     * @param y
     * @return 
     */
    private boolean isDelete(int x, int y) {
        if (data[x][y].getGray() == 255) {
            return false;
        }

        float value1 = data[x - 1][y - 1].getGray();
        float value2 = data[x + 1][y - 1].getGray();
        if ((value1 != value2) && ((data[x - 1][y].getGray() == value1) && (data[x - 1][y + 1].getGray() == value1)) && ((data[x + 1][y].getGray() == value2) && (data[x + 1][y + 1].getGray() == value2))) {
            return true;
        }

        value1 = data[x - 1][y - 1].getGray();
        value2 = data[x + 1][y + 1].getGray();
        if ((value1 != value2) && ((data[x][y - 1].getGray() == value1) && (data[x - 1][y].getGray() == value1)) && ((data[x + 1][y].getGray() == value2) && (data[x][y + 1].getGray() == value2))) {
            return true;
        }

        value1 = data[x - 1][y + 1].getGray();
        value2 = data[x + 1][y - 1].getGray();
        if ((value1 != value2) && ((data[x][y + 1].getGray() == value1) && (data[x - 1][y].getGray() == value1)) && ((data[x + 1][y].getGray() == value2) && (data[x][y - 1].getGray() == value2))) {
            return true;
        }

        return false;
    }

    /**
     * Xóa các chi tiết thừa
     */
    private void clearBone() {
        for (int i = 1; i < height - 1; ++i) {
            for (int j = 1; j < width - 1; ++j) {
                if (data[i][j].getGray() == 0) {
                    if (data[i - 1][j - 1].getGray() == 0) {
                        data[i - 1][j] = new Pixel(255, 255, 255);
                        data[i][j - 1] = new Pixel(255, 255, 255);
                    }
                    //
                    if (data[i - 1][j + 1].getGray() == 0) {
                        data[i - 1][j] = new Pixel(255, 255, 255);
                        data[i][j + 1] = new Pixel(255, 255, 255);
                    }
                    //
                    if (data[i + 1][j - 1].getGray() == 0) {
                        data[i + 1][j] = new Pixel(255, 255, 255);
                        data[i][j - 1] = new Pixel(255, 255, 255);
                    }
                    //
                    if (data[i + 1][j + 1].getGray() == 0) {
                        data[i + 1][j] = new Pixel(255, 255, 255);
                        data[i][j + 1] = new Pixel(255, 255, 255);
                    }
                }
            }
        }
    }

    /**
     * Tạo ảnh xương
     * @return 
     */
    public Pixel[][] getBoneImage() {
        getBinaryImage();
        //
        while (!allBone()) {
            for (int i = 1; i < height - 1; ++i) {
                for (int j = 1; j < width - 1; ++j) {
                    if (isBorder(i, j) && isDelete(i, j)) {
                        data[i][j] = new Pixel(255, 255, 255);
                    }
                }
            }

        }
        clearBone();
        return data;
    }

    /**
     * Kiểm tra xem đã hết điểm biên chưa xóa hay chưa
     * @return 
     */
    private boolean allBone() {
        for (int i = 1; i < height - 1; ++i) {
            for (int j = 1; j < width - 1; ++j) {
                if (isBorder(i, j) && isDelete(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // kiểm tra điểm vân có phải là chi tiết đặc biệt hay không
    private boolean isMinutiae(int x, int y) {
        if (data[x][y].getGray() == 255) {
            return false;
        }
        float sum;
        sum = Math.abs(data[x - 1][y - 1].getGray() - data[x - 1][y].getGray())
                + Math.abs(data[x - 1][y + 1].getGray() - data[x][y + 1].getGray())
                + Math.abs(data[x + 1][y + 1].getGray() - data[x + 1][y].getGray())
                + Math.abs(data[x + 1][y - 1].getGray() - data[x][y - 1].getGray());
        sum /= 255;
        if (sum == 1 || sum == 3) {
            return true;
        }
        return false;
    }

    /**
     * Tạo ảnh hiển thị chi tiết đặc biệt
     * @return 
     */
    public Pixel[][] getMinutiaeImage() {
        getBoneImage();
        for (int i = 1; i < height - 1; ++i) {
            for (int j = 1; j < width - 1; ++j) {
                if (isMinutiae(i, j)) {
                    // Đánh dấu các điểm chi tiết đặc biệt bằng màu đỏ  
                    data[i][j] = new Pixel(255, 0, 0);
                }
            }
        }
        return data;    
    }

    public void getCharacterized() {
        double[] angelSet = new double[21];   // Tap cac goc quay
        double pi = (-1 * Math.PI / 6);
        int step = 0;
        for (int i = 0; i < 21; ++i) {
            angelSet[i] = pi + step;
            step += (Math.PI / 60);
        }
        //
        int value;
        int[] deltaXSet = new int[height / 2];
        value = -1 * height;
        step = 0;
        for (int i = 0; i < height / 2; ++i) {
            deltaXSet[i] = value;
            step += 2;
        }
        //
        int[] deltaYSet = new int[width / 2];
        value = -1 * width;
        step = 0;
        for (int i = 0; i < width / 2; ++i) {
            deltaYSet[i] = value;
            step += 2;
        }
        //

    }
}