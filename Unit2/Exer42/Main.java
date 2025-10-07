package Unit2.Exer42;

public class Main {
    public static void main(String[] args) {
        String path = null;
        if (args != null && args.length > 0) {
            path = args[0];
        } else {
            try {
                byte[] inputBuffer = new byte[4096];
                int len = System.in.read(inputBuffer);
                if (len > 0) {
                    String raw = new String(inputBuffer, 0, len).trim();
                    path = raw.split("\r?\n")[0].trim();
                }
            } catch (Exception ignored) {
                // If stdin read fails, we'll handle null path below
            }
        }

        if (path == null || path.isEmpty()) {
            System.out.println("Usage: java Unit2.Exer42.Main <file_path> (or pass the path via stdin)");
            return;
        }

        String type = detectImageType(path);
        System.out.println(type);
    }

    private static String detectImageType(String filePath) {
        java.io.File file = new java.io.File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "File not found";
        }

        byte[] header = new byte[8];
        int read = 0;
        try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
            read = fis.read(header);
        } catch (Exception e) {
            return "Error reading file";
        }

        if (read < 2) {
            return "Unknown";
        }

        // BMP: 42 4D
        if (read >= 2 && (header[0] & 0xFF) == 0x42 && (header[1] & 0xFF) == 0x4D) {
            return ".BMP";
        }

        // GIF: 47 49 46 38 39 61 (GIF89a) or 47 49 46 38 37 61 (GIF87a)
        if (read >= 6 &&
            (header[0] & 0xFF) == 0x47 && (header[1] & 0xFF) == 0x49 && (header[2] & 0xFF) == 0x46 &&
            (header[3] & 0xFF) == 0x38 &&
            (((header[4] & 0xFF) == 0x39 && (header[5] & 0xFF) == 0x61) ||
             ((header[4] & 0xFF) == 0x37 && (header[5] & 0xFF) == 0x61))) {
            return ".GIF";
        }

        // ICO: 00 00 01 00
        if (read >= 4 &&
            (header[0] & 0xFF) == 0x00 && (header[1] & 0xFF) == 0x00 &&
            (header[2] & 0xFF) == 0x01 && (header[3] & 0xFF) == 0x00) {
            return ".ICO";
        }

        // JPEG: FF D8 FF
        if (read >= 3 &&
            (header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8 && (header[2] & 0xFF) == 0xFF) {
            return ".JPEG";
        }

        // PNG: 89 50 4E 47
        if (read >= 4 &&
            (header[0] & 0xFF) == 0x89 && (header[1] & 0xFF) == 0x50 &&
            (header[2] & 0xFF) == 0x4E && (header[3] & 0xFF) == 0x47) {
            return ".PNG";
        }

        return "Unknown";
    }
}
