# 仿小红书移动端全栈项目

本项目是一个仿小红书手机端的完整项目，采用前后端分离架构，并实现 100% 容器化部署。

## 🛠 技术栈
- Frontend: Vue 3 + TypeScript + Vant UI + Pinia
- Backend: Spring Boot 3 + MyBatis + Maven
- Database: MySQL 8.0

## 🚀 启动指南 (How to Run)
1. 确保 Docker Desktop 已启动。
2. 在根目录执行：`docker compose up --build`
3. 等待容器启动完成...

## 🔗 服务地址 (Services)
- Frontend: http://localhost:3000 (建议开启浏览器 F12 移动端模拟)
- Backend API: http://localhost:8080/api
- Database: localhost:3306 (user: root / pass: root)

## 🧪 测试账号
- Admin: admin / 123456
- Test User: test_user / 123456

## ✨ 核心功能
- **首页**: 瀑布流布局，支持分类切换，流畅的加载体验。
- **笔记**: 详情查看、点赞、收藏、发表评论。
- **搜索**: 支持关键词搜索笔记。
- **用户**: 注册、登录、个人中心、编辑资料、上传头像。
- **发布**: 支持图文发布笔记。

---

## 🐳 Docker 镜像源配置 (Docker Registry Configuration)

### 推荐配置

#### 1. Docker 镜像源
使用官方 Docker Hub 镜像（已验证稳定可用）。

#### 2. npm 依赖源
使用淘宝镜像以加速依赖安装：
```dockerfile
RUN npm config set registry https://registry.npmmirror.com
```

#### 3. Maven 依赖源
使用阿里云镜像以加速 Java 构建，配置已包含在 `backend/settings.xml` 中。

#### 4. 前端构建加速规范
1. **本地预处理**: 在提交代码前，已在本地运行 `npm install` 确保 `package-lock.json` 文件存在。
2. **锁文件提交**: `package-lock.json` 已提交至仓库，确保构建一致性。
3. **容器内安装**: `Dockerfile` 中使用 `npm ci` 代替 `npm install` 以提升构建速度。
