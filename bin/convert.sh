#!/bin/zsh

# 環境に応じて利用者が設定
seburl={URL}
sebid={ID}
anstemplate={Template File Path}

# パラメーターチェック
if [[ "$1" = "" ]]; then
    echo "$(basename $0) targetYearMonth(yyyy-MM) [outputFile]"
    exit 1
fi
# パラメーターの出力ファイル名省略時はデフォルトのファイル名とする
ansoutput=${2:-output.xlsx}

# 実行時に入力
read -s "sebpassword?input password: "
echo

scriptDir=$(cd $(dirname $0); pwd)
opts="-Dwebdriver.chrome.driver=$scriptDir/chromedriver"
opts="$opts -Dseb.url=$seburl"
opts="$opts -Dseb.id=$sebid"
opts="$opts -Dseb.password=$sebpassword"
opts="$opts -Dans.templateFile=$scriptDir/$anstemplate"
opts="$opts -Dans.outputFile=$ansoutput"

# 実行
java ${=opts} -jar $scriptDir/attendance-report-converter-0.0.1.jar --targetYearMonth=$1

exit 0
