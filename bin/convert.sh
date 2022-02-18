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

# 実行時に入力
read -s "sebpassword?input password: "
echo

scriptDir=$(cd $(dirname $0); pwd)
opts="-Dwebdriver.chrome.driver=$scriptDir/chromedriver"
opts="$opts -Dseb.url=$seburl"
opts="$opts -Dseb.id=$sebid"
opts="$opts -Dseb.password=$sebpassword"
opts="$opts -Dans.templateFile=$scriptDir/$anstemplate"
opts="$opts -Dans.outputFile=$2"

# 実行
java ${=opts} -jar $scriptDir/attendance-report-converter-0.0.1-SNAPSHOT.jar --targetYearMonth=$1

exit 0
