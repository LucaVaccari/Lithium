for /f "tokens=1 delims=." %%a in ('dir /B *.aac') do (
	if not exist "./%%a" mkdir "./%%a"
	ffmpeg -i %%a.aac -f segment -segment_time 6 -hls_time 6 -c copy ./%%a/%%a%%03d.aac
	java -jar M3U8Creator-1.0.jar "./%%a"
)
pause