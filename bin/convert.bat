@echo off
setlocal

rem 環境に応じて利用者が設定
set seburl={URL}
set sebid={ID}
set anstemplate={Template File Path}

rem パラメーターチェック
if "%~1" == "" (
    echo %~nx0 targetYearMonth(yyyy-MM^) [outputFile]
    exit /b 1
)
rem パラメーターの出力ファイル名省略時はデフォルトのファイル名とする
set ansoutput=%~2
if "%ansoutput%" == "" set ansoutput=output.xlsx

rem 実行時に入力
set /p sebpassword="input password: "

set scriptDir=%~dp0
set opts=-Dwebdriver.chrome.driver=%scriptDir%/chromedriver.exe
set opts=%opts% -Dseb.url=%seburl%
set opts=%opts% -Dseb.id=%sebid%
set opts=%opts% -Dseb.password=%sebpassword%
set opts=%opts% -Dans.templateFile=%scriptDir%/%anstemplate%
set opts=%opts% -Dans.outputFile=%ansoutput%

rem 実行
java %opts% -jar %scriptDir%/attendance-report-converter-0.0.1-SNAPSHOT.jar --targetYearMonth=%~1

endlocal
exit /b 0
