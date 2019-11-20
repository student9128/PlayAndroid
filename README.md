# PlayAndroid

AndroidX

### 提交记录
1. 2019/11/20 添加基类，工具类,使用

### 开发中的问题
1. DrawerLayout滑动返回，需要将NavigationView放到布局最后才可以。
2. DrawerLayout延伸到状态的问题，单独为MainActivity设置主题，项目中可以看到主题的设置，以及NavigationView的配置。
3. AppBarConfiguration会报错提示："Cannot inline bytecode built with JVM target 1.8 into bytecode that is being built with JVM target 1.6"，
解决：gradle 添加
```
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).all {
                       kotlinOptions {
                           jvmTarget = "1.8"
                       }
                   }
```
