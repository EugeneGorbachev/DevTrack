package tracker;

import controller.MainFromController;

import java.awt.*;
import java.net.URL;

public class TrayIconController {
    public static TrayIcon createTrayIcon(URL imageURL, MainFromController mainFromController) throws Exception {
        if (SystemTray.isSupported()) {
            PopupMenu popupMenu = new PopupMenu();
            MenuItem startItem = new MenuItem("Start");
            startItem.addActionListener(e -> {
                if (!mainFromController.buttonStartTracking.isDisable()) {
                    mainFromController.buttonStartTracking.fire();
                }
            });
            MenuItem stopItem = new MenuItem("Stop");
            stopItem.addActionListener(e -> {
                if (!mainFromController.buttonStopTracking.isDisable()) {
                    mainFromController.buttonStopTracking.fire();
                }
            });
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> {
                mainFromController.buttonStopTracking.fire();
                System.exit(0);
            });

            popupMenu.add(startItem);
            popupMenu.add(stopItem);
            popupMenu.add(exitItem);

            Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
            return new TrayIcon(image, "Task Tracking", popupMenu);
        } else {
            throw new Exception("System tray is not supported");
        }
    }

    public static void addIconToTray(TrayIcon trayIcon) throws Exception {
        if (trayIcon != null) {
            if (SystemTray.isSupported()) {
                SystemTray systemTray = SystemTray.getSystemTray();
                systemTray.add(trayIcon);
            } else {
                throw new Exception("System tray is not supported");
            }
        }
    }

    public static void removeTrayIcon(TrayIcon trayIcon) throws Exception {
        if (trayIcon != null) {
            if (SystemTray.isSupported()) {
                SystemTray systemTray = SystemTray.getSystemTray();
                systemTray.remove(trayIcon);
            } else {
                throw new Exception("System tray is not supported");
            }
        }
    }

    public static void showMessage(TrayIcon trayIcon, String message, TrayIcon.MessageType messageType) throws Exception {
        if (trayIcon != null) {
            if (SystemTray.isSupported()) {
                trayIcon.displayMessage("Time tracking", message, messageType);
            } else {
                throw new Exception("System tray is not supported");
            }
        }
    }
}
