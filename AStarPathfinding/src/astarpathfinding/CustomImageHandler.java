package astarpathfinding;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.LINK;
import javax.swing.TransferHandler.TransferSupport;

public class CustomImageHandler extends TransferHandler {

    private static final long serialVersionUID = 1;

    private boolean isReadableByImageIO(DataFlavor flavor) {
        Iterator<?> readers = ImageIO.getImageReadersByMIMEType(
                flavor.getMimeType());
        if (readers.hasNext()) {
            Class<?> cls = flavor.getRepresentationClass();
            return (InputStream.class.isAssignableFrom(cls)
                    || URL.class.isAssignableFrom(cls)
                    || File.class.isAssignableFrom(cls));
        }

        return false;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (support.getUserDropAction() == LINK) {
            return false;
        }

        for (DataFlavor flavor : support.getDataFlavors()) {
            if (flavor.equals(DataFlavor.imageFlavor)
                    || flavor.equals(DataFlavor.javaFileListFlavor)
                    || isReadableByImageIO(flavor)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!(support.getComponent() instanceof JPanel)) {
            return false;
        }
        if (!canImport(support)) {
            return false;
        }

        // There are three types of DataFlavor to check:
        // 1. A java.awt.Image object (DataFlavor.imageFlavor)
        // 2. A List<File> object (DataFlavor.javaFileListFlavor)
        // 3. Binary data with an image/* MIME type.
        if (support.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                BufferedImage image = (BufferedImage) support.getTransferable().getTransferData(
                        DataFlavor.imageFlavor);

               AStarPathfinding.SetImage(image);
                return true;
            } catch (UnsupportedFlavorException | IOException e) {
               System.out.println("Unsupported file: " + e);
            }
        }

        if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try {
                Iterable<?> list = (Iterable<?>) support.getTransferable().getTransferData(
                        DataFlavor.javaFileListFlavor);
                Iterator<?> files = list.iterator();
                if (files.hasNext()) {
                  
                        File file = (File) files.next();
                        BufferedImage image = ImageIO.read(file);
                     //  System.out.println(file.getName());
                        AStarPathfinding.SetImage(image);
//                        JLabel label = (JLabel) support.getComponent();
//                        label.setIcon(new ImageIcon(image));
                        return true;
                   

                }
            } catch (UnsupportedFlavorException | IOException e) {
                System.out.println("Unsupported file: " + e);
               
            }
        }

        for (DataFlavor flavor : support.getDataFlavors()) {
            if (isReadableByImageIO(flavor)) {
                try {
                    BufferedImage image;

                    Object data
                            = support.getTransferable().getTransferData(flavor);
                    if (data instanceof URL) {
                        image = ImageIO.read((URL) data);
                    } else if (data instanceof File) {
                        image = ImageIO.read((File) data);
                    } else {
                        image = ImageIO.read((InputStream) data);
                    }

                     AStarPathfinding.SetImage(image);
                    return true;
                } catch (UnsupportedFlavorException | IOException e) {
                   System.out.println("Unsupported file: " + e);
                }
            }
        }

        return false;
    }
}
