
import org.jpy.*;

public class JPyExample {
    public static void main(String[] args) {
        // Start the Python interpreter
        PyLib.startPython();

        // Call the Python function
        PyModule sys = PyModule.importModule("sys");

        PyObject path = sys.getAttribute("path");
        path.call("append", "/home/usama/AntMedia/Plugins/SamplePlugin/python_wrapper");
        PyModule dumdum = PyModule.importModule("script");

        PyObject pyFunc = dumdum.call("onVideoFrame", "221");
        String result = pyFunc.toString();
        System.out.println("Result: " + result);

        // Stop the Python interpreter
        PyLib.stopPython();
    }
}

//javac -cp jpy.jar -d . JPyExample.java

//java  -Djpy.jpyLib=/home/usama/Pictures/jpy/build/lib.linux-x86_64-3.8/jpy.cpython-38-x86_64-linux-gnu.so -Djpy.jdlLib=/home/usama/Pictures/jpy/build/lib.linux-x86_64-3.8/jdl.cpython-38-x86_64-linux-gnu.so -Djpy.pythonLib=/snap/gnome-3-38-2004/119/usr/lib/python3.8/config-3.8-x86_64-linux-gnu/libpython3.8.so -cp "./:./jpy.jar" org.jpy.JPyExample

