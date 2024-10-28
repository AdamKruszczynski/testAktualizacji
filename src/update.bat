@echo off
set appPath=%1

rem Usuń starą wersję aplikacji
rmdir /s /q "%appPath%"

rem Przenieś nową wersję do głównego katalogu aplikacji
move /y "sciezka_do_nowej_wersji" "%appPath%"

rem Uruchom nową wersję
start "" "%appPath%\nowa_wersja_aplikacji.exe"
exit