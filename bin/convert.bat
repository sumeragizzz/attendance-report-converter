@echo off
setlocal

rem 環境に応じて利用者が設定
set seburl={URL}
set sebid={ID}
set anstemplate={Template File Path}

rem パラメーターチェック
if "%~1" == "" (
    echo %~nx0 targetYearMonth(yyyy-MM^) [outputFile]
    exit /b -1
)

rem 実行時に入力
set /p sebpassword="input password: "

set options="-Dwebdriver.chrome.driver=chromedriver.exe"
set options=%options% "-Dseb.url=%seburl%"
set options=%options% "-Dseb.id=%sebid%"
set options=%options% "-Dseb.password=%sebpassword%"
set options=%options% "-Dans.templateFile=%anstemplate%"
set options=%options% "-Dans.outputFile=%~2"

java %options% -jar %~dp0/attendance-report-converter-0.0.1-SNAPSHOT.jar --targetYearMonth=%~1

endlocal
exit /b 0
