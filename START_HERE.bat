@echo off
chcp 65001 >nul
cls
echo ====================================
echo    MXT EAV 项目启动向导
echo ====================================
echo.
echo [1] 启动后端服务 (Spring Boot)
echo [2] 打开前端页面 (快速测试版)
echo [3] 同时启动后端和前端
echo [0] 退出
echo.
set /p choice=请选择操作 (0-3): 

if "%choice%"=="1" goto backend
if "%choice%"=="2" goto frontend
if "%choice%"=="3" goto both
if "%choice%"=="0" goto end

:backend
echo.
echo 正在启动后端服务...
echo 请确保已安装 JDK 17+ 和 Maven 3.8+
echo.
mvn spring-boot:run
goto end

:frontend
echo.
echo 正在打开前端页面...
start "" "mxt-eav-frontend\quick-start.html"
echo.
echo 前端页面已在浏览器中打开！
echo 注意：需要先启动后端服务才能正常使用
echo.
pause
goto end

:both
echo.
echo 正在启动后端和前端...
echo.
start "MXT EAV Backend" cmd /k "cd /d %~dp0 && mvn spring-boot:run"
echo 后端服务正在启动中...
timeout /t 5 >nul
echo 正在打开前端页面...
start "" "mxt-eav-frontend\quick-start.html"
echo.
echo ====================================
echo   启动完成！
echo ====================================
echo.
echo 后端地址：http://localhost:8080
echo 前端地址：直接打开 mxt-eav-frontend\quick-start.html
echo H2控制台：http://localhost:8080/h2-console
echo.
echo H2数据库连接信息：
echo - JDBC URL: jdbc:h2:mem:mxt_eav
echo - 用户名: sa
echo - 密码: (留空)
echo.
pause

:end
echo.
echo 再见！
