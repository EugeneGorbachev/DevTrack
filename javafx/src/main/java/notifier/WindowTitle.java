package notifier;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

public class WindowTitle {
    public static String getActiveWindowTitle() {
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        int titleLength = User32.INSTANCE.GetWindowTextLength(hwnd) + 1;
        char[] title = new char[titleLength];
        User32.INSTANCE.GetWindowText(hwnd, title, titleLength);
        return Native.toString(title);
    }
}
