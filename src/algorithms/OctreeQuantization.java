package algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import interfaces.Algorithm;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class OctreeQuantization implements Algorithm {

    private static class OctreeNode {
        int redSum;
        int greenSum;
        int blueSum;
        int pixelCount;
        OctreeNode[] children;

        OctreeNode() {
            redSum = 0;
            greenSum = 0;
            blueSum = 0;
            pixelCount = 0;
            children = new OctreeNode[8];
        }
    }

    @Override
    public Image startAlgorithm(Image image, int colorCount) {
        // Convert the image to a buffered image
        BufferedImage bufferedImage = convertToBufferedImage(image);

        // Convert the buffered image to an indexed image using octree quantization
        OctreeNode root = buildOctree(bufferedImage);
        List<Color> palette = generatePalette(root, colorCount);
        IndexColorModel colorModel = createColorModel(palette);
        BufferedImage indexedImage = createIndexedBufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel);

        // Assign indexed colors to the image pixels
        assignColors(bufferedImage, indexedImage, root);

        // Convert the indexed image back to Image
        return convertToImage(indexedImage);
    }

    private static BufferedImage convertToBufferedImage(Image image) {
      

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        return bufferedImage;
    }

    private static Image convertToImage(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    
    

    private static OctreeNode buildOctree(BufferedImage image) {
        OctreeNode root = new OctreeNode();

        // Construct the octree
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb);
                insertColor(root, color.getRed(), color.getGreen(), color.getBlue());
            }
        }

        return root;
    }

    private static void insertColor(OctreeNode node, int red, int green, int blue) {
        if (node.children[0] == null) {
            // Leaf node, update the color sums and pixel count
            node.redSum += red;
            node.greenSum += green;
            node.blueSum += blue;
            node.pixelCount++;
        } else {
            // Recurse to the appropriate child node
            int index = getIndex(red, green, blue);
            OctreeNode child = node.children[index];
            if (child == null) {
                child = new OctreeNode();
                node.children[index] = child;
            }
            insertColor(child, red, green, blue);
        }
    }

    private static int getIndex(int red, int green, int blue) {
        int index = 0;
        if (red >= 128) index |= 1;
        if (green >= 128) index |= 2;
        if (blue >= 128) index |= 4;
        return index;
    }

    private static List<Color> generatePalette(OctreeNode node, int colorCount) {
        List<Color> palette = new ArrayList<>();
        PriorityQueue<OctreeNode> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.pixelCount));

        // Add all leaf nodes to the priority queue
        queue.add(node);

        // Retrieve the top colorCount nodes from the priority queue
        while (palette.size() < colorCount && !queue.isEmpty()) {
            OctreeNode topNode = queue.poll();
            if (topNode.pixelCount > 0) {
                palette.add(averageColor(topNode));
            } else {
                for (OctreeNode child : topNode.children) {
                    if (child != null) {
                        queue.add(child);
                    }
                }
            }
        }

        return palette;
    }

    private static Color averageColor(OctreeNode node) {
        int red = node.redSum / node.pixelCount;
        int green = node.greenSum / node.pixelCount;
        int blue = node.blueSum / node.pixelCount;
        return new Color(red, green, blue);
    }

    private static IndexColorModel createColorModel(List<Color> palette) {
        int size = palette.size();
        byte[] r = new byte[size];
        byte[] g = new byte[size];
        byte[] b = new byte[size];

        for (int i = 0; i < size; i++) {
            Color color = palette.get(i);
            r[i] = (byte) color.getRed();
            g[i] = (byte) color.getGreen();
            b[i] = (byte) color.getBlue();
        }

        return new IndexColorModel(8, size, r, g, b);
    }

    private static BufferedImage createIndexedBufferedImage(int width, int height, IndexColorModel colorModel) {
        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, colorModel);
    }

    private static void assignColors(BufferedImage sourceImage, BufferedImage indexedImage, OctreeNode node) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = sourceImage.getRGB(x, y);
                Color color = new Color(rgb);
                Color indexedColor = findNearestColor(color, node);
                indexedImage.setRGB(x, y, indexedColor.getRGB());
            }
        }
    }

    private static Color findNearestColor(Color color, OctreeNode node) {
        if (node.pixelCount > 0) {
            // Found a leaf node, return its average color
            return averageColor(node);
        } else {
            // Recurse to the appropriate child node
            int index = getIndex(color.getRed(), color.getGreen(), color.getBlue());
            OctreeNode child = node.children[index];
            if (child != null) {
                return findNearestColor(color, child);
            } else {
                // No child node, search for the nearest non-null child
                for (int i = 0; i < node.children.length; i++) {
                    if (node.children[i] != null) {
                        return findNearestColor(color, node.children[i]);
                    }
                }
            }
        }
        return null; // Default to null if no suitable node is found
    }

    
}
