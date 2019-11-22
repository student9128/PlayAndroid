# PlayAndroid

AndroidX

### 提交记录
3. 2019/11/22 添加网络框架和一些基类，去掉对Navigation的使用，改用原始的Fragment切换方法，并优化从DrawerLayout跳转Activity的问题
2. 2019/11/21 提交关于Navigation的使用，但是由于根据不够灵活，后面取消使用，改用原始方法
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
4. DrawerLayout在配合Navigation使用的过程中，发现点击NavigationView的Item不会切换，后来发现两个原因：
    - menu和fragment的id不同
    - 使用了 ``navView.setNavigationItemSelectedListener(this)``，重写了``override fun onNavigationItemSelected(item: MenuItem): Boolean {}``该方法，导致失效.</br>
    正常写法就是仿照Android Studio新建的项目DrawerLayout的使用即可

```
class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val states =
        arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))
    private lateinit var colors: IntArray
    private lateinit var colorStateList: ColorStateList
    override fun initView() {
        setContentView(R.layout.activity_main)
        initToolbar()
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_project, R.id.nav_navigation,
                R.id.nav_official_account
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

//这里不要添加监听，否则失效。但是如果我们需要自定义操作，那么还是添加监听，自己写吧
//        navView.setNavigationItemSelectedListener(this)
//
//        colors = intArrayOf(Color.GRAY, ContextCompat.getColor(this, R.color.colorAccent))
//        colorStateList = ColorStateList(states, colors)
//        navView.itemTextColor = colorStateList
//        navView.itemIconTintList = colorStateList
//        setNavItemCheck(navView.menu.getItem(0))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//这里由于BaseActivity对该按钮做了返回操作，故这里重写
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START, true)
        }
        return true
    }

}
```
5. 对于工具类的封装：适合自己的就是最好的。java开发时候都封装过SP，之前看到别人的Kotlin对于SP的封装，就搜索了下，
发现他们都是把所要存储的key值写到了工具类，有些写了一些高阶函数，但是依然避免不了set,get。或许是水平有限吧，不知道
是不是有其他优势，但个人感觉不好用，依旧采用之前的方法写，稍作改变。