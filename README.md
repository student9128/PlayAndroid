# PlayAndroid

AndroidX

### 部分提交记录
7.2019/11/28 首页添加刷新和头部轮播

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
10. 登录注册界面在小屏幕上面弹出键盘会出现Toolbar上移的问题，解决方案：将EditText放在ScrollView里面，Toolbar放在外面即可解决
11. RecyclerView结合Paging使用添加header和footer的问题：解决方案：网上搜索到的文章，https://juejin.im/post/5caa0052f265da24ea7d3c2c，并且解决了添加头布局刷新的问题。
12. 使用了第三方的Banner库，地址：https://github.com/zhpanvip/BannerViewPager.
使用的时候 `viewPager.create(d)`一定要放在初始化设置参数的后面
```
    fun bindTo(d: List<BannerData>) {
            viewPager.setAutoPlay(true)
                .setInterval(5000)
                .setPageMargin(DisplayUtils.dp2px(10f))
                .setPageStyle(PageStyle.MULTI_PAGE_SCALE)
                .setHolderCreator(HolderCreator { BannerViewHolder() })
                .setIndicatorColor(ContextCompat.getColor(context,R.color.gray),ContextCompat.getColor(context,R.color.colorPrimary))
                .setIndicatorMargin(0,0,0,DisplayUtils.dp2px(16f))
            viewPager.create(d)
        }
```
13. RecyclerView快速滑动到顶部的时候，所提供的方法`scrollToPosition()`是直接到顶部效果不好，`smoothScrollToPosition()`是匀速滑动到顶部，到下滑的多了这个方法就不行了需要很长时间。解决方案：根据当前索能看到的第一条目和所要滑动到的也就是顶部第一条的距离，设置一个最大滑动条目，大于这个距离就先直接调用`scrollToPosition()`，再`smoothScrollToPosition()`
```
    /**
     * 该方法的文章地址
     * https://carlrice.io/blog/better-smoothscrollto
     */
    private fun RecyclerView.betterSmoothScrollToPosition(targetItem: Int) {
        layoutManager?.apply {
            val maxScroll = 20
            when (this) {
                is LinearLayoutManager -> {
                    val topItem = findFirstVisibleItemPosition()
                    val distance = topItem - targetItem
                    val anchorItem = when {
                        distance > maxScroll -> targetItem + maxScroll
                        distance < -maxScroll -> targetItem - maxScroll
                        else -> topItem
                    }
                    if (anchorItem != topItem) scrollToPosition(anchorItem)
                    post {
                        smoothScrollToPosition(targetItem)
                    }
                }
                else -> smoothScrollToPosition(targetItem)
            }
        }
    }
```
14. RecyclerView局部刷新闪动，解决方案
```
  fun updateItem(position: Int, d: DataX) {
        data[position] = d
        //payload解决局部刷新闪动问题
        notifyItemChanged(position, "payload$position")
    }
```