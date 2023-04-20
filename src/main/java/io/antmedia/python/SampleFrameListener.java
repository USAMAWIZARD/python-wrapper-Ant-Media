
package io.antmedia.app;

import org.jpy.*;
import org.bytedeco.ffmpeg.global.avutil.*;
import org.bytedeco.javacpp.BytePointer;
import java.awt.image.BufferedImage;
import org.bytedeco.ffmpeg.avutil.AVFrame;
import java.awt.Color;
import io.antmedia.plugin.AVFramePool;
import io.antmedia.plugin.Utils;
import io.antmedia.plugin.api.IFrameListener;
import io.antmedia.plugin.api.StreamParametersInfo;
import java.io.IOException;
import static org.bytedeco.ffmpeg.global.avutil.av_frame_ref;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class SampleFrameListener implements IFrameListener {

	private int audioFrameCount = 0;
	private int videoFrameCount = 0;
	PyModule dumdum;
	private AVFrame yuvFrame;

	@Override
	public AVFrame onAudioFrame(String streamId, AVFrame audioFrame) {
		audioFrameCount++;
		return audioFrame;
	}

	@Override
	public AVFrame onVideoFrame(String streamId, AVFrame videoFrame) {
		videoFrameCount++;

		 System.out.println("Result----------------------------------------------: " );
		// result);

		if (yuvFrame != null) {
			AVFramePool.getInstance().addFrame2Pool(yuvFrame);
		}

		int format = videoFrame.format();

		AVFrame cloneframe = AVFramePool.getInstance().getAVFrame();
		av_frame_ref(cloneframe, videoFrame);

		AVFrame rgbFrame = Utils.toRGB(cloneframe);
		AVFramePool.getInstance().addFrame2Pool(cloneframe);

		byte[] RGBAdata = new byte[rgbFrame.width() * rgbFrame.height() * 4];
		rgbFrame.data(0).get(RGBAdata);
		int height = rgbFrame.height();
		int width = rgbFrame.width();

		PyObject pyFunc = dumdum.call("onVideoFrame",streamId, rgbFrame.width(), rgbFrame.height(), RGBAdata);
		String result = pyFunc.toString();

		
		
		BufferedImage image;
		try {
	        byte[] imageData = Base64.getDecoder().decode(result);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
			image = ImageIO.read(bis);
			bis.close();

			if (image != null) {
				byte[] data = Utils.getRGBData(image);
				rgbFrame.data(0, new BytePointer(data));
				yuvFrame = Utils.toTargetFormat(rgbFrame, format);
				AVFramePool.getInstance().addFrame2Pool(rgbFrame);
				return yuvFrame;
			}

		  } catch (Exception e) {
			e.printStackTrace();
		  }
		  		
	

		return videoFrame;
	}

	@Override
	public void writeTrailer(String streamId) {
		System.out.println("SampleFrameListener.writeTrailer()");
	}

	@Override
	public void setVideoStreamInfo(String streamId, StreamParametersInfo videoStreamInfo) {
		System.out.println(" pythton SampleFrameListener.setVideoStreamInfo()");

		PyLib.startPython();

		// Call the Python function
		PyModule sys = PyModule.importModule("sys");

		PyObject path = sys.getAttribute("path");
		path.call("append", "/home/usama/AntMedia/Plugins/SamplePlugin/python_wrapper");
		dumdum = PyModule.importModule("script");
	}

	@Override
	public void setAudioStreamInfo(String streamId, StreamParametersInfo audioStreamInfo) {
		System.out.println("SampleFrameListener.setAudioStreamInfo()");
	}

	@Override
	public void start() {
		System.out.println("SampleFrameListener.start()");
	}

	public String getStats() {
		return "audio frames:" + audioFrameCount + "\t" + "video frames:" + videoFrameCount;
	}

}
