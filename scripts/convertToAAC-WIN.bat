for /f "tokens=1 delims=." %%a in ('dir /B *.mp3') do ffmpeg -i "%%a.mp3" -c:a aac -b:a 128k "%%a.aac"
pause