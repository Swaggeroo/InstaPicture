import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    ArrayList<File> images = new ArrayList<File>();

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        File[] dir = new File("./pics/").listFiles();
        if (dir != null) {
            for (File file : dir){
                if (file.isFile()){
                    if (file.getName().endsWith(".png")||file.getName().endsWith(".jpg")){
                        images.add(file);
                    }
                }
            }
        }

        for (File img : images){
            System.out.println(img.getAbsolutePath());
        }

        try {
            BufferedImage bufferedImage = ImageIO.read(images.get(7));
            float width = bufferedImage.getWidth();
            int height = (int)((width/16)*9);
            bufferedImage = bufferedImage.getSubimage(0,0,bufferedImage.getWidth(),height);

            BufferedImage instaPic = putOnPlainWhite(bufferedImage);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File outputfile = new File("./new.jpg");
            ImageIO.write(instaPic, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private BufferedImage putOnPlainWhite(BufferedImage bufferedImage){
        BufferedImage instaPic = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getWidth(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = instaPic.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, instaPic.getWidth(), instaPic.getHeight());
        g2.drawImage(bufferedImage,0,((bufferedImage.getWidth()- bufferedImage.getHeight())/2),null);
        g2.dispose();
        return instaPic;
    }

    private void showImage(BufferedImage bufferedImage){
        JFrame f = new JFrame();
        f.setBackground(Color.BLACK);
        f.setSize(new Dimension(900,900));
        f.add(jPanel);
        f.setVisible(true);
    }
}
