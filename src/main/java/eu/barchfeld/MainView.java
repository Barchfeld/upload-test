package eu.barchfeld;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// see https://github.com/DiegoSanzVi/saving_displaying_images_db

@Route
@PWA(name = "Programm zur AKten Organisaton", shortName = "PAKO")
public class MainView extends VerticalLayout {

    private VerticalLayout imageContainer;

    PatImage patImage = new PatImage();
    Image image = new Image();
    Long key;

    @Autowired
    private PatImageRepository patImageRepository;

    public MainView(@Autowired MessageBean bean) {

        MemoryBuffer picBuffer = new MemoryBuffer();
        Upload picUpload = new Upload( picBuffer );
        picUpload.setMaxFiles(1);
        picUpload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png");

        picUpload.addSucceededListener(event -> {
            //String attachmentName = event.getFileName();
            try {
                // The image can be jpg png or gif, but we store it always as png file in this example
                BufferedImage inputImage = ImageIO.read(picBuffer.getInputStream());
                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                ImageIO.write(inputImage, "png", pngContent);
                saveProfilePicture(pngContent.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        add(picUpload);

        Button button = new Button("Click me", evt -> {
                patImage = patImageRepository.findWithPropertyPictureAttachedById(key);
                image = generateImage(patImage);
                image.setHeight("100%");
                imageContainer.removeAll();
                imageContainer.add(image);
            }
        );

        add(button);

        initImageContainer();

    }


    private void saveProfilePicture(byte[] imageBytes) {
        patImage.setPatPicture(imageBytes);
        key = patImageRepository.save(patImage).getId();
    }


    private Image convertToImage(byte[] imageData)
    {
        StreamResource streamResource = new StreamResource("isr", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(imageData);
            }
        });
        return new Image(streamResource, "photo");
    }

    public Image generateImage(PatImage patImage) {
        Long id = patImage.getId();
        StreamResource sr = new StreamResource("patImage", () ->  {
            PatImage attached = patImageRepository.findWithPropertyPictureAttachedById(id);
            return new ByteArrayInputStream(attached.getPatPicture());
        });
        sr.setContentType("image/png");
        Image image = new Image(sr, "Radiologieaufnahem");
        return image;
    }

    private void initImageContainer(){
        imageContainer = new VerticalLayout();
        imageContainer.setWidth("200px");
        imageContainer.setHeight("200px");
        imageContainer.getStyle().set("overflow-x", "auto");
        add(imageContainer);
    }
}
