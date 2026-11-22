# Tarnhelm-R

这是 [Tarnhelm](https://github.com/lz233/Tarnhelm) 的个人 Fork，主要进行项目维护、兼容性改进及问题修复。

## 更新内容

- **兼容性修复**
  - 修复小米 HyperOS 4（Android 17）环境下 Bridge Service 绑定异常的问题，避免 LSPosed 日志出现错误，并恢复模块正常使用。
  - 优化服务绑定、解绑及异常重连逻辑。

- **构建系统调整**
  - 将项目构建脚本迁移至 Kotlin DSL，并使用 Version Catalog 统一管理依赖。
  - 更新 Android Gradle Plugin、Gradle、Kotlin 及相关依赖。
  - 将 `compileSdk` 和 `targetSdk` 升级至 37。
  - 将 Java 工具链及 Gradle Daemon JVM 迁移至 Java 21。

- **代码优化**
  - 替换过时的 Android API。
  - 清理未使用的代码及依赖。
  - 优化部分代码结构和构建配置。

## 上游项目

- 原项目：[lz233/Tarnhelm](https://github.com/lz233/Tarnhelm)