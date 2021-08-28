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
        //read files in input
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

        //Generate a date String for output filename
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        String strDate = dateFormat.format(date);

        //process every picture
        for (int i = 0; i < images.size(); i++){
            try {
                BufferedImage bufferedImage = ImageIO.read(images.get(i));
                //get sizes for 16:9
                float width = bufferedImage.getWidth();
                int height = (int)((width/16)*9);
                //if picture is wider than 16:9
                if (height < bufferedImage.getHeight()){
                    //crop image in center
                    bufferedImage = bufferedImage.getSubimage(0,(bufferedImage.getHeight()-height)/2,bufferedImage.getWidth(),height);
                }else {
                    //recalculate sizes
                    height = bufferedImage.getHeight();
                    int newWidth = (int) ((((float) height)/9)*16);
                    //crop image in center
                    bufferedImage = bufferedImage.getSubimage((bufferedImage.getWidth()-newWidth)/2,0,newWidth,height);
                }

                BufferedImage instaPic = putOnPlainWhite(bufferedImage);

                //Save new picture
                File outputfile = new File("./output/insta"+strDate+"-"+(i+1)+".png");
                ImageIO.write(instaPic, "png", outputfile);
                System.out.println((i+1) + "/" + images.size() + "\t\t" + outputfile.getName()+"\tCreated! ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BufferedImage putOnPlainWhite(BufferedImage bufferedImage){
        //make an empty square image
        BufferedImage instaPic = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getWidth(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = instaPic.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, instaPic.getWidth(), instaPic.getHeight());
        //put image in center
        g2.drawImage(bufferedImage,0,((bufferedImage.getWidth()- bufferedImage.getHeight())/2),null);
        g2.dispose();
        return instaPic;
    }

    //showing processed image in Window (method for debugging)
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

    //scale image down for showing in window (method for debugging)
    private BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
