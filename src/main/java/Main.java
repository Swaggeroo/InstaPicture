import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Main {

    ArrayList<File> images = new ArrayList<>();

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        File[] dir = new File("input/").listFiles();
        if (dir != null) {
            int count = 0;
            for (File file : dir){
                if (file.isFile()){
                    if (file.getName().endsWith(".png")||file.getName().endsWith(".jpg")||file.getName().endsWith(".JPG")){
                        count++;
                        System.out.println("Found "+count+": "+file.getAbsolutePath());
                        images.add(file);
                    }
                }
            }
        }


        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        String strDate = dateFormat.format(date);

        for (int i = 0; i < images.size(); i++){
            try {
                BufferedImage bufferedImage = ImageIO.read(images.get(i));
                float width = bufferedImage.getWidth();
                int height = (int)((width/16)*9);
                bufferedImage = bufferedImage.getSubimage(0,0,bufferedImage.getWidth(),height);

                BufferedImage instaPic = putOnPlainWhite(bufferedImage);
                //showImage(instaPic);

                File outputfile = new File("./output/insta"+strDate+"-"+(i+1)+".png");
                ImageIO.write(instaPic, "png", outputfile);
                System.out.println((i+1) + "/" + images.size() + "\t\t" + outputfile.getName()+"\tCreated! ");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        JLabel picLabel = new JLabel(new ImageIcon(getScaledImage(bufferedImage,800,800)));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        JFrame f = new JFrame();
        f.setBackground(Color.BLACK);
        f.setSize(new Dimension(900,900));
        f.add(jPanel);
        f.setVisible(true);
    }
}
