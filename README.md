# PlayAndroid

AndroidX

### 提交记录
6.2019/11/26 使用AndroidStudio 4.0 Canary 4 升级Gradle版本为4.0.0-alpha04
5.2019/11/26 对viewModel和Paging的使用进行优化，添加网络请求状态，首页界面UI优化
4.2019/11/25 初步实现使用Coroutines进行数据请求，通过viewModel和Paging进行处理展示数据。下一步，对数据请求和jetPack的使用进行封装

3.2019/11/22 添加网络框架和一些基类，去掉对Navigation的使用，改用原始的Fragment切换方法，并优化从DrawerLayout跳转Activity的问题

2.2019/11/21 提交关于Navigation的使用，但是由于根据不够灵活，后面取消使用，改用原始方法

1.2019/11/20 添加基类，工具类,使用

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
6. 使用代码控制条目选中状态：在使用NavigationView的时候，我通过自定义显示点击的条目的颜色，可以通过xml的select来实现。但是发现这里使用代码区实现更简单一些。在网上看到java的实现方式 
```
 int[][] states = new int[][] {
        new int[]{-android.R.attr.state_pressed}, // not pressed
        new int[] { android.R.attr.state_pressed}  // pressed
    };
    int[] colors = new int[] {
        foregroundColor,
        accentColor,
        accentColor
    };
  ColorStateList myList = new ColorStateList(states, colors);
```
那么Kotlin怎么实现呢，这里就涉及到了Kotlin二维数组的使用，由于好久没用过了也是在网上苦搜了一把
```
  val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked))
  val colors = intArrayOf(Color.GRAY, ContextCompat.getColor(this, R.color.colorAccent))
  var colorStateList = ColorStateList(states, colors)
```
7. 对Coroutines初步使用，起初一直困惑CoroutineScope的用法，经过搜索发现需要进行实现该接口，并重写 ```public val coroutineContext: CoroutineContext```。对与ViewModel和Paging的使用还在摸索中，暂时根据网上文章进行实现，后面进一步封装和深入理解。
8. ViewModel配合Paging使用，网络请求在Paging的PageKeyedDataSource中进行，这时候需要将网络请求状态返给ViewModel,z这里添加了一个`progressStatus：MutableLiveData<String>`，ViewModel则通过`homeLiveData: MutableLiveData<HomeDataSource>` 拿到`HomeDataSource`，再通过`Transformations.switchMap`装换出来所需要的progressStatus，然后再在Fragment或者Activity拿到这个状态进行页面替换展示。
9. 升级Gradle版本过程遇到错误
```
ERROR: Could not find org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.60-eap-25
```
解决方案：Kotlin版本改为1.3.60-eap-25，build.gradle文件中添加
```
 maven { url 'https://dl.bintray.com/kotlin/kotlin-eap' }
```