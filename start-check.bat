@echo off
chcp 65001 >nul
echo ==============================================
echo       MXT EAV 系统启动检查
echo ==============================================
echo.
echo [1] 后端服务检查...
timeout /t 1 >nul
echo.
echo [后端地址] http://localhost:8080
echo [H2 控制台] http://localhost:8080/h2-console
echo.
echo [2] 前端服务检查...
timeout /t 1 >nul
echo.
echo [前端简单页面] 请手动打开:
echo mxt-eav-frontend\quick-start.html
echo.
echo [前端React应用] 如已启动: http://localhost:3000
echo.
echo ==============================================
echo 正在打开简单HTML页面...
start "" "mxt-eav-frontend\quick-start.html"
echo.
echo 如果要查看完整的React应用，请等待前端服务启动
echo 或在新的终端窗口中运行:
echo cd mxt-eav-frontend
echo pnpm start
echo.
pause
