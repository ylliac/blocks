@echo off

REM Get the script path
set scriptPath=%~f0
echo %scriptPath%

REM Get the script name
set scriptName=%0
echo %scriptName%

REM Get the script name without extension
set scriptSimpleName=%~n0
echo %scriptSimpleName%

REM Get the script directory with the final \
set scriptDirectory=%~dp0
echo %scriptDirectory%

REM Get the current directory with the final \
set currentDirectory=%CD%\
echo %currentDirectory% 

REM Get the property file name
set propertyFile=%scriptDirectory%test.txt

REM Read the property file line by line 
echo -- Read properties from %propertyFile%
for /f "tokens=*" %%a in (%propertyFile%) do call :defineProperty %%a

REM Ask the user properties which were not loaded from property file
call :getOrAskProperty titi "TITI ? "
call :getOrAskProperty maven "Dossier d'installation de Maven (exemple : C:\Appli\apache_maven)"

REM Wait for input
pause

goto:eof

:getOrAskProperty

set varName=%~1
set varDescription=%~2

IF DEFINED %varName% goto:eof
    
echo %varDescription% (%varName%) : 
set /p varValue=
call :defineProperty %varName% %varValue%

goto:eof

:defineProperty

set key=%~1
set value=%~2

echo -- Set property %key% = %value%

set %key%=%value%

goto:eof

