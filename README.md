[TOC]

***由于采用 Typora 书写，github 不支持部分格式，建议 clone 后用 Typora 观看***

# 自定义View与动画

## 一、自定义View

### 1、为什么要自定义？

> 一句话：官方的控件满足不了产品想要的效果 (
>
> 但其实，官方控件已经能满足绝大多数需求了，正常情况下自定义 View 是下下策，因为造自定义 View 的轮子是一件很耗时也很耗精力的事。
>
> 这里分享一下我大一暑假时遇到的事情，当时共 5 个组分开做积分商城，产品给了一个积分商城的设计图，其中主页有一个上滑展开的需求，我们组（我和钟智），我就负责做这个上滑的界面。当时我花了好像 3、4 天的时间去设计一个自定义 View（原谅我当时还不知道协调者布局这个东西），结果啊，产品改需求了，靠
>
> 经过上面的这个故事后，相信大家应该理解到了选择自定义 View 会面临的风险
>
> 后来我又花了接近一周的时间还是用自定义 View 写出了产品的新需求，限于当时我的能力，现在去看当时写的代码，存在一些耦合的地方，算是给以后学弟留坑了😁
>
> OK，回到这里，自定义 View 确实是下下策，不止会影响你的心情，如果你的代码设计得不好的话（比如耦合度很高，代码逻辑混乱），还会影响以后维护代码人的心情。（说个笑话：听说代码写得越烂，越不容易被辞退 :）

### 2、分类

> 这里自定义 View 是一个习惯性的统称
>
> 如果按照类型来分：
>
> - 自定义 View
>
>   > 指直接或间接继承于 View 的控件，如：TextView、Button
>   >
>   > ```java
>   > public class TextView extends View {}
>   > ```
>   >
>   > ```java
>   > public class Button extends TextView {}
>   > ```
>   >
>   > 
>
> - 自定义 ViewGroup
>
>   > ```java
>   > public class LinearLayout extends ViewGroup {}
>   > ```
>   >
>   > ```java
>   > public class FrameLayout extends ViewGroup {}
>   > ```
>
> 
>
> 如果按照学习的内容来分，大致分为以下内容
>
> - View 的测量与布局
>
>   > 测量就是与 Measure 相关的方法，如：`onMeasure()`
>   >
>   > 布局就是与 Layout 相关的方法，如：`onLayout()`
>   >
>   > 这两个一般是在一起调用的，并且通常是在 自定义 ViewGroup 中才会涉及到（View 中也可以实现，比如 TextView 就重写了 `onLayout()` 方法）
>
> - View 的绘制
>
>   > 绘制就是与 Draw 相关的方法，如：`onDraw()`
>   >
>   > 为什么要列出绘制？
>   >
>   > 因为很多动画都是依靠重写绘制来实现的，所以绘制也比较重要
>
> - 事件分发
>
>   >一般指触摸事件的分发
>   >
>   >这个应该是自定义 View 中最难也最重要的内容了，如果你想设计出一些很炫酷的界面，就得精通事件分发
>   >
>   >事件分发会在下下节课讲解
>
> 接下来就是正式开始讲解自定义 View 的时间，由于时间原因，在加上你们的现阶段能力的原因，本节课更偏向于讲解如何使用自定义 View，而不是讲解底层内容，底层内容以博客的形式分享

### 3、构造函数

> 该部分内容参考：https://www.cnblogs.com/angeldevil/p/3479431.html#three
>
> View 一共有四个构造函数
>
> ```java
> public class View {
> // 这个用于在代码中直接 new 一个 View，这个 context 一般都是 activity，代表上下文
> public View(Context context)
> 
> // 这个用于在 xml 中书写，系统自动将你写在 xml 中的属性装换为一个 AttributeSet 对象，然后调用这个函数
> public View(
> Context context, 
> @Nullable AttributeSet attrs
> )
> 
> // 这个一般用于设置默认属性，使用的情况较少
> // 系统不会主动调用，一般是开发者自己设置
> public View(
> Context context, 
> @Nullable AttributeSet attrs, 
> int defStyleAttr
> )
> 
> // 这个与上面的类似，也是设置默认属性，使用的情况也较少，且只支持 Android 5.0 以上
> // 系统不会主动调用
> public View(
> Context context, 
> @Nullable AttributeSet attrs, 
> int defStyleAttr, 
> int defStyleRes
> )
> }
> 
> ```
>
> 前面两个构造函数各位应该都能看懂，关键在于后面两个，他们与设置默认属性相关，在讲解前我们先得知道一个 View 可以通过哪些方式设置属性

#### 1、直接写在 XML 中

> ```xml
> <androidx.cardview.widget.CardView
> android:layout_width="200dp"
> android:layout_height="200dp"
> app:cardBackgroundColor="@android:color/darker_gray"
> app:cardCornerRadius="8dp"/>
> ```
>
> 这样就会给当前 View 设置属性
>
> 原理是系统自动把这些属性写入到 AttributeSet 类里面，然后调用
>
> ```kotlin
> public View(Context context, @Nullable AttributeSet attrs)
> ```
>
> 来生成 View 对象
>
> 

#### 2、使用 @style 设置属性

> 在 `style.xml` 中：
>
> ```xml
> <style name="myCardView_style">
>     <item name="cardBackgroundColor">@android:color/darker_gray</item>
>     <item name="cardCornerRadius">8dp</item>
> </style>
> ```
>
> 在 `layout.xml` 中：
>
> ```xml
> <androidx.cardview.widget.CardView
>  android:layout_width="200dp"
>  android:layout_height="200dp"
>  style="@style/myCardView_style"/>
> ```
>
> 这个一般用于多个相同控件复用属性的时候
>
> 原理与上一个是一样的，它会把 `style="@style/MyCardView"` 里写的属性一起写入 AttributeSet 类里面
>
> > 问题：如果 `style="@style/MyCardView"` 与 xml 中有相同属性它会怎么处理呢？
> >
> > 经过测试后，xml 中定义的属性会覆盖 `style="@style/MyCardView"` 中定义的属性
> >
> > 

#### 3、在 theme 中设置某种控件的默认属性

> 在 `theme.xml` 中：
>
> ```xml
> <style name="MyAppTheme" parent="Theme.MaterialComponents">
>  <!--下面这个 @style/MyCardView 就是前面写的 <style name="MyCardView">-->
>  <item name="cardViewStyle">@style/myCardView_style</item>
> </style>
> ```
>
> 这里 `name="cardViewStyle"` 意思是定义 CardView 的默认属性
> 这个默认属性是如何生效的呢？
>
> ```java
> // 原因在于 CardView 的构造函数
> public CardView(
> @NonNull Context context, 
> @Nullable AttributeSet attrs
> ) {
> this(context, attrs, R.attr.cardViewStyle);
> }
> 
> public CardView(
> @NonNull Context context, 
> @Nullable AttributeSet attrs, 
> int defStyleAttr
> ) {
> super(context, attrs, defStyleAttr);
> }
> ```
>
> 这里我们可以发现，两个参数的构造函数使用了 `this(context, attrs, R.attr.cardViewStyle)` 调用了三个参数的构造函数，其中他给第三个参数传入了一个值 `R.attr.cardViewStyle`，我们去扒它源码，看下这是什么东西（直接点击导包中的 `import androidx.cardview.R` 就可以跳到源码中）
>
> ```xml
> <resources>
> <attr format="reference" name="cardViewStyle"/>
> </resources>
> ```
>
> 这里可能你们会看不懂，我简单讲一下：这个 `format="reference"` 代表这个属性接受的类型，而 `reference` 表示接受的类型为一个引用值，比如：`@style/xxx`、`@color/xxx`、`@drawable/xxx` 就是引用值
>
> 然后 `name="cardViewStyle"` 代表这个属性的唯一名字
>
> 这个 attr 的引用就是 CardView 可以在 theme 中设置默认属性的原因，因为系统调用 CardView 两个参数的构造函数，然后它这个构造函数主动调用了三个参数的构造函数，并且传入了 `R.attr.cardViewStyle`，所以构造函数的第三个参数对应的是一个`style` 格式的引用，但需要你自己在 View 中指定名字
>
> 引用 `theme.xml` 有以下几种方式：
>
> - 全局引用，所有 View 都会使用该配置
>
>   ```xml
>   <application
>       android:theme="@style/MyAppTheme">
>   </application>
>   ```
>
> - 某个 Activity 引用，当前 Activity 内使用该配置
>
>   ```xml
>   <activity
>       android:name=".section3.test.TestActivity"
>       android:theme="@style/MyAppTheme"
>       android:exported="false" />
>   ```
>
> - 某个 Activity 使用代码引用
>
>   ```kotlin
>   override fun onCreate(savedInstanceState: Bundle?) {
>       super.onCreate(savedInstanceState)
>       setTheme(R.style.MyAppTheme)
>       setContentView(R.layout.layout_card0)
>   }
>   // 注意在 Activity 中使用必须在 setContentView 之前
>   ```
>
> - 单个 View 使用
>
>   ```xml
>   <androidx.cardview.widget.CardView
>       android:layout_width="200dp"
>       android:layout_height="200dp"
>       android:theme="@style/MyAppTheme" />
>   ```
>
> 看到这里你可能有点晕，我们简单梳理一下流程：
>
> ```mermaid
> graph TB
> id1("系统读取你写的 xml")-->id2
> id2["生成 AttributeSet，然后调用两个参数的构造函数"]-->id3
> id3["两个参数的调用三个参数的，并传入 R.attr.cardViewStyle"]-->id4
> id4["View 开始从 theme 中读取属性"]-->id5
> id5["View 在 theme 中发现 name=cardViewStyle>xxxx< 的定义"]-->id6
> id6("读取 @style/MyCardView 里面的属性")
> ```
>
> 

#### 4、调用构造函数的 defStyleRes

> 由于 CardView 没有重写第四个构造函数（可能是为了兼容 Android 5.0 以下？），所以这里我用 FrameLayout 来演示
>
> 先在 `style.xml` 中定义属性：
>
> ```xml
> <style name="myFrameLayout">
>  <item name="android:layout_width">match_parent</item>
>  <item name="android:layout_height">match_parent</item>
>  <item name="android:background">@android:color/black</item>
> </style>
> ```
>
> 然后在代码中这样使用：
>
> ```kotlin
> val frameLayout = FrameLayout(this, null, 0, R.style.myFrameLayout_style)
> ```
>
> 当你不能在 xml 中申明，只能在代码中动态生成时，就可以使用这种写法，有个优点就是使用 `style.xml` 可以进行属性的复用，但一般都用不到复用，这时候就可以使用 LayoutParams 和调用对应方法来代替：
>
> ```kotlin
> val frameLayout = FrameLayout(this)
> // Android 第一节课我们讲过，父布局可以给子布局设置属性
> // 且属性都以 layout_ 开头，所以 layout_width 和 layout_height 
> // 对应由 LayoutParams 来设置
> frameLayout.layoutParams = ViewGroup.LayoutParams(
>  ViewGroup.LayoutParams.MATCH_PARENT,
>  ViewGroup.LayoutParams.MATCH_PARENT
> )
> // background 属于 View 自身属性，所以对应 View 自身方法
> frameLayout.setBackgroundColor(Color.BLACK)
> 
> // 但这样写有一个缺点就是：LayoutParams 这东西只能设置少量的属性，
> // 而且 LayoutParams 有很多个，具体选择那个需要看你的 View 被添加到
> // 哪种父布局里，如果是 LinearLayout，就是 LinearLayout.LayoutParams(...)
> ```
>
> 可能有人不知道 LayoutParams 是什么东西，你暂时可以把它看成是用来保存 View 中有关**父布局属性**的一个数据类，供父布局使用，后面会再次讲解
>
> 除了上面这种动态生成时传入默认属性，你也可以在构造器中设置你的自定义 View 的默认属性：
>
> 一般是这样定义构造器的：
>
> ```kotlin
> // 由于 java 是基础，我先用 java 来定义构造器，后面再使用 kt 来优化写法
> public class Section3View extends View {
>  public Section3View(Context context) {
>      // 注意这里调用的是 this，而不是 super
>      this(context, null);
>  }
> 
>  public Section3View(
>      Context context, 
>      @Nullable AttributeSet attrs
>  ) {
>      // 注意这里调用的是 this，而不是 super
>      this(context, attrs, R.attr.mySection3View_attrs);
>  }
> 
>  public Section3View(
>      Context context, 
>      @Nullable AttributeSet attrs, 
>      int defStyleAttr
>  ) {
>      // 注意这里调用的是 this，而不是 super
>      // 这里就是设置默认属性
>      this(
>          context, 
>          attrs, 
>          defStyleAttr, 
>          R.style.mySection3View_defaultAttrs
>      );
>  }
> 
>  public Section3View(
>      Context context, 
>      @Nullable AttributeSet attrs, 
>      int defStyleAttr, 
>      int defStyleRes
>  ) {
>      // 直到这里才调用 super
>      super(context, attrs, defStyleAttr, defStyleRes);
>  }
> }
> ```
>
> 其中 `R.attr.mySection3View_attrs`：
>
> ```xml
> <!--value/attrs.xml-->
> <resources>
>  <attr name="mySection3View_attrs" format="reference"/>
> </resources>
> ```
>
> 其中 `R.style.mySection3View_defaultAttrs`：
>
> ```xml
> <!--value/style.xml-->
> <style name="mySection3View_defaultAttrs">
>  <item name="android:background">@android:color/black</item>
> </style>
> ```
>
> 如果你去对照官方控件的写法，你会发现他们的构造函数也是这样写的，前三个都是调用 `this()`，只有最后一个调用   `super()`，但他们一般只设置了 `R.attr.mySection3View_attrs`，用于开发者在 theme 中定义全局属性（[第三点](#3、在 theme 中设置某种控件的默认属性)），而 `R.style.mySection3View_defaultAttrs` 在官网控件中一般没设置，都是写个 0，代表没得默认属性
>
> > 为什么有两个默认属性？
> >
> > ```java
> > public View(
> > Context context, 
> > @Nullable AttributeSet attrs, 
> > int defStyleAttr, 
> > int defStyleRes
> > )
> > ```
> >
> > 你会发现第三个参数 `defStyleAttr` 和第四个 `defStyleRes` 都是设置默认参数，但为什么要同时存在两个呢？
> >
> > 原因：`defStyleAttr` 是从 theme 中读取默认值，但如果我们只能动态生成 View，又需要多次用到一些属性，在 Android 5.0 之前要么每次都手动调用方法来设置，要么写在 theme 中，但这样未免麻烦了些，在 Android 5.0 后增加了这个 `defStyleRes` 参数，就可以直接把属性写在 `style.xml` 中，方便复用
> >
> > **但这里有很重要的一点：**使用 `defStyleRes` 时不能设置 `defStyleAttr`，给 `defStyleAttr` 填入 0 或者 theme 中不设置对应的属性即可
>
> 回到这里，前面 java 的写法确实感觉有些臃肿了，我们来看看 kt 的写法：
>
> ```kotlin
> // 必须添加 @JvmOverloads，不然 java 层无法使用该构造器
> // 会导致 xml 中的 View 无法生成
> class Section3ViewKt @JvmOverloads constructor(
>     context: Context,
>     attrs: AttributeSet? = null,
>     defStyleAttr: Int = R.attr.mySection3View_attrs,
>     defStyleRes: Int = R.style.mySection3View_defaultAttrs
> ) : View(context, attrs, defStyleAttr, defStyleRes) {
> }
> // 如果你没得 R.attr.mySection3View 或 R.style.MySection3View_defaultAttrs
> // 可以直接赋值为 0，就是没得默认属性设置
> ```
>
> **讲到这里，构造函数中的四个参数各位应该知道有什么作用了，这里我们总结一下：**
>
> ```java
> public View(Context context)
> // 用于在代码中动态生成自定义 View
> ```
>
> ```java
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs
> )
> // 用于写在 xml 里面系统调用
> ```
>
> ```java
> public View(
>     Context context,
>     @Nullable AttributeSet attrs, 
>     int defStyleAttr
> )
> // 用于设置全局默认属性值，通常是通过上面两个参数的构造函数来间接调用
> ```
>
> ```java
> public View(
>     Context context, 
>     @Nullable AttributeSet attrs, 
>     int defStyleAttr, 
>     int defStyleRes
> )
> // 也是用于设置默认属性值，只是不用写在 theme 中，通常用于动态生成代码中或自己自定义 View 的时候
> ```
>
> 前面讲了 4 种方式来设置属性，还有一种方式也可以来设置属性
>

#### 5、直接在 theme 中定义属性

> 这个与第三点：[在 theme 中设置某种控件的默认属性](#3、在 theme 中设置某种控件的默认属性) 有些类似，但它值允许直接写在 theme 中
>
> ```xml
> <resources xmlns:tools="http://schemas.android.com/tools">
>     <style name="Theme.CustomViewWithAnimStudy" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
>         <!--如果你加了这行代码，你会发现应用大部分控件变成黑色了-->
>         <item name="android:background">@android:color/black</item>
>     </style>
> </resources>
> ```
>
> **但是，**这个是不分控件的给全部 View 设置属性，一般不会使用到它，只有设置整个应用的主题时会用到
>
> 比如你可以试试把这几个属性改一下，就会发现应用的标题栏颜色从紫色改成了你设置的颜色
>
> ```xml
> <item name="colorPrimary">@color/purple_500</item>
> <item name="colorPrimaryVariant">@color/purple_700</item>
> <item name="colorOnPrimary">@color/white</item>
> <item name="colorSecondary">@color/teal_200</item>
> <item name="colorSecondaryVariant">@color/teal_700</item>
> <item name="colorOnSecondary">@color/black</item>
> ```
>
> 这些属性对应了某种具体位置的颜色，可以看看这篇博客：https://blog.csdn.net/smartzzg/article/details/104788412
>
> ### 五种属性定义方法的顺序
>
> > 我们讲了有 5 种不同的属性定义，如果定义了相同属性，他们存在一个优先级顺序：
> >
> > [1、直接写在 XML 中](#1、直接写在 XML 中)
> >
> > [2、使用 @style 设置属性](#2、使用 @style 设置属性)
> >
> > [3、在 theme 中设置某种控件的默认属性](#3、在 theme 中设置某种控件的默认属性)
> >
> > [4、调用构造函数的 defStyleRes](#4、调用构造函数的 defStyleRes)
> >
> > [5、直接在 theme 中定义属性](#5、直接在 theme 中定义属性)
> >
> > 这里注意：使用 `defStyleRes ` 时不能设置 `defStyleAttr`，给 `defStyleAttr` 填入 0 或者 theme 中不设置对应的属性即可
>
> **到这里 View 的四个构造函数基本分析完毕了，接下来我们开始讲解里面的方法**

### 4、onDraw()

> ### 作用
>
> 对 View 进行绘制
>
> ### 涉及内容
>
> - [Canvas 画布](#1、Canvas 画布)
> - [Paint 画笔](#2、Paint 画笔)
> - [Path 路径](#3、Path 路径)
>
> ```kotlin
> // 该方法是一个回调，为什么是回调，在之后的 invalidate() 会进行讲解
> override fun onDraw(canvas: Canvas?) {
> super.onDraw(canvas)
> }
> ```
>
> > 这里讲一个小技巧：可以把 `Canvas?` 中的 `?` 为去掉
> >
> > 因为在 kt 重写 `onDraw()` 方法时，源代码 java 层中并没有给参数 `canvas` 加上 `@NonNull`，导致 kt 不知道它会不会为空，但我们作为开发者肯定是知道它不会为空的，所以去掉 `?`，可以减少后续的判断，不然很可能写出这样的代码
> >
> > ```kotlin
> > override fun onDraw(canvas: Canvas?) {
> >     super.onDraw(canvas)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> >     canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> > }
> > ```
> >
> > 看着好像没得什么问题，不就多打了一个 `?` 吗？
> >
> > 确实看着没有什么问题，但你把 kt 转成 java，你就会发现：
> >
> > ```java
> > protected void onDraw(@Nullable Canvas canvas) {
> >    super.onDraw(canvas);
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> >    if (canvas != null) {
> >       canvas.drawRect(0.0F, 0.0F, 100.0F, 200.0F, this.mBlackPaint);
> >    }
> > }
> > ```
> >
> > 每个 canvas 都单独判断了一遍是否为空，不知道你们怎么想，但我的强迫症不允许出现这种情况，所以建议各位写自定义 View 时，把方法参数中可以去掉 `?` 的顺手给去掉
> >
> > 除了 `onDraw()` 以外，还有 `override fun onTouchEvent(event: MotionEvent?)` 的 `?` 也可以去掉
> >
> > > 怎么看 kt 转成的 java 代码？
> > >
> > >  <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320161207313.png" alt="image-20220320161207313" style="zoom:50%;" />
> > >
> > >  <img src="C:/Users/%E9%83%AD%E7%A5%A5%E7%91%9E/AppData/Roaming/Typora/typora-user-images/image-20220320144121673.png" alt="image-20220320144121673" style="zoom:50%;" />
> >
> > 接下来我们开始讲解 `onDraw()` 需要的其他知识
>

#### 1、Canvas 画布

> 由于时间原因，这里不会讲得很详细，你们可以在这些地方进行学习：
>
> - https://carsonho.blog.csdn.net/article/details/60598775
> - https://qijian.blog.csdn.net/article/details/50995268
>
>
> | 参数         | 类型   | 作用                                                         |
> | ------------ | ------ | ------------------------------------------------------------ |
> | ***canvas*** | Canvas | 字面翻译是“帆布”，你可以看成是系统底层给了你一张“画布”（canvas），然后你在这张画布上进行绘图 |
>
> 那怎么进行画图呢？
>
> ```kotlin
> // 这个 paint 你可以把它看成是“画笔”
> private val mBlackPaint = Paint().apply {
> color = Color.BLACK // 设置画笔颜色为黑色
> }
> override fun onDraw(canvas: Canvas) {
> super.onDraw(canvas)
> // 画一个 (0, 0) - (100, 200) 的黑色矩形
> canvas.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
> }
> ```
>
> 如上面代码，调用 `canvas.drawRect()` 就可以画一个黑色矩形在 View 里面
>
> 接下来，我们看一下效果：
>
> <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320144833039.png" alt="image-20220320144833039" style="zoom:50%;float:left" />
>
> 如果你没有自定义 View 基础的话，可能会觉得有些奇怪
>
> 我们从小学开始学的坐标系第一象限不是向上为 Y 正半轴，向右为 X 正半轴，那么按照惯性思维，(0, 0) - (100, 200) 为什么不是下图这样的？
>
> <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320144833039.png" alt="image-20220320144833039" style="zoom:50%;transform:rotateX(180deg);float:left" />
>
> 其实原因在于 View 中坐标系的 y 轴是反过来的，我猜测是因为：为了设配手机的特点，比如我们看一些列表，都是手指往上滑动，查看下面的内容，所以为了好开发，View 中的坐标原点就设置在了左上角，向下为 Y 正半轴，向右为 X 正半轴
>
> 这个算是基础内容了，如果不知道的话，可以去看看这篇文章：https://blog.csdn.net/carson_ho/article/details/56009827
>
> Canvas 里面不止有绘制矩形的方法，还有绘制圆的方法，绘制文字的方法······，这里不会去讲解这些基础内容，可以去下面这些地方去查看：
>
> - https://developer.android.google.cn/reference/kotlin/androidx/compose/ui/graphics/Canvas?hl=en

#### 2、Paint 画笔

> 前面的代码演示中出现了 `Paint`，这个类主要是管画笔的，除了设置颜色以外，他还可以设置线的粗细等
>
> ```kotlin
> // 这是我的某个自定义 View 设置的 Paint
> private val mCircleBackgroundPaint by lazyUnlock {
>     Paint().apply {
>         color = mCircleBackground
>         style = Paint.Style.FILL
>         isAntiAlias = true
>     }
> }
> ```
>
> Paint 的方法有很多，这里给出文章，你们有时间可以去看看，那本自定义黑书对于 Paint 的内容有点少，建议去看书作者写的文章，里面有很多其他高级用法
>
> - https://qijian.blog.csdn.net/article/details/50995268

#### 3、Path 路径

> Path 表示路径，常用于绘制曲线，绘制不规则的图形等
>
> ```kotlin
> // 这是我的某个自定义 View 设置的 Path
> // 主要是写了根据变化的 offset 值来计算圆的半径，最后绘制一个圆
> private fun drawFirstCircle(path: Path, offset: Float, total: Float) {
>     val radio = abs(offset / total)
>     val startMove = 0.6F
>     val k = 1 / (1 - startMove)
>     val b = 1 - k
>     val y = offset / abs(offset) * max(0F, k * radio + b) * total
>     val r = getNewRadius(y)
>     val dx = (outerX - abs(y)) / (outerR + r) * r
>     val dy = outerY / (outerR + r) * r
>     firstPointX = if (offset > 0) dx + y else -dx + y
>     firstPointY = dy
>     path.addCircle(y, 0F, r, Path.Direction.CCW)
> }
> ```
>
> 方法有很多，这里不进行讲解，给出下面文章：
>
> - https://developer.android.google.cn/reference/kotlin/androidx/compose/ui/graphics/Path?hl=en
> - https://carsonho.blog.csdn.net/article/details/60597923
> - https://qijian.blog.csdn.net/article/details/50995268

#### 4、其他东西

> 上面并没有列完基础知识，因为基础知识实在是太多了，这里是写不下的，接下来我讲一些其他经验性的东西

##### 1、onDraw() 里面禁止 new 对象

> 很多初学者在刚开始写的时候很喜欢在 `onDraw()` 里面 new 对象，之前 `canvas.drawRect()` 不是要传入一个画笔吗？就会有人这样写
>
> ```kotlin
> override fun onDraw(canvas: Canvas) {
>  super.onDraw(canvas)
>  val blackPaint = Paint()
>  blackPaint.color = Color.BLACK
>  canvas.drawRect(0F, 0F, 100F, 200F, blackPaint)
> }
> ```
>
> 这样写时 AS 还会报一个黄，告诉你这样写是不对的，但你知道为什么不对吗？
>
> 原因：`onDraw()` 在一直刷新视图时，在 60 帧的手机上会每隔 16 毫秒回调一次，90 帧的手机上每隔 11 毫秒回调一次（1000 ÷ 90），所以在一秒钟，60 帧的手机会生成 60 个 Paint 对象，90 帧手机生成 90 个对象，这样疯狂 new 对象，会让手机出现卡顿，这跟上节课讲的 `onBindViewHolder()` 不要写点击监听一样，但这个会比它更严重
>
> 但 AS 的智能提示并不是能发现所有的问题，比如下面这样：
>
> ```kotlin
> override fun onDraw(canvas: Canvas) {
>  super.onDraw(canvas)
>  set { 
>      // ...
>  }
> }
> 
> private fun set(func: () -> Unit) {
>  func.invoke()
> }
> ```
>
> 这种情况较难发现，这样写每次都会生成一个匿名内部类，建议加上 `inline` 关键字
>
> **该禁令同样适用于 `onMeasure()`、`onLayout()`！**

##### 2、不要在 onDraw() 里面进行耗时操作

> 原因跟上面一样
>
> **该禁令同样适用于 `onMeasure()`、`onLayout()`！**

##### 3、不要持有 canvas 对象

> 每次回调的 canvas 对象并不是一定是同一个（大部分情况下是同一个），而且每次的回调只能表示当前帧的状态，比如我们来个违规操作：
>
> ```kotlin
> override fun onDraw(canvas: Canvas) {
>  super.onDraw(canvas)
>  // 开一个线程模拟持有 canvas 对象
>  thread {
>      // 延迟 20 毫秒才绘图
>      sleep(20)
>      canvas.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
>  }
> }
> ```
>
> 你可能猜都猜不到它把图绘制到哪里去了
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> 
>
> <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320163052101.png" alt="image-20220320163052101" style="zoom:50%;float:left" />
>
> 好家伙，它竟然把图绘制到标题栏上了，这里猜测原因如下（没有去查看源码找真正原因）：
>
> - 整个应用共用了同一个 canvas 对象
> - 因开启线程后 sleep，所以 canvas 是整个应用绘制完成后再调用的 `canvas.drawRect()`
> - **标题栏是在最后进行绘制的**，此时的 canvas 的坐标系是以标题栏为准的
>
> 这怎么验证呢？
>
> > 你把开发者模式模式的 `显示布局边界` 给打开，你会看到下面这种图：
> >
> > <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320164459116.png" alt="image-20220320164459116" style="zoom: 67%;float:left" />
> >
> > 这里说明标题栏的文字是一个 View 来显示的
> >
> > 那我们试试把标题栏给去掉会发生什么？
> >
> > ```xml
> > <!--theme.xml-->
> > <style name="xxx" parent="Theme.MaterialComponents.DayNight.NoActionBar">
> > ```
> >
> > <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320164844556.png" alt="image-20220320164844556" style="zoom: 67%;float:left" />
> >
> > 嘿，去掉标题栏后绘制的位置对了，所以这里我提出猜测：标题栏是在最后进行绘制的
> >
> > 其实我们可以验证一下这个猜测，写一个在它后面绘制的 View 验证下：
> >
> > ```xml
> > <com.ndhzs.lib.section4.MySection4ViewCanvas
> >  android:layout_width="300dp"
> >  android:layout_height="300dp"
> >  app:layout_constraintBottom_toBottomOf="parent"
> >  app:layout_constraintLeft_toLeftOf="parent"
> >  app:layout_constraintRight_toRightOf="parent"
> >  app:layout_constraintTop_toTopOf="parent" />
> > 
> > <View
> >  android:layout_width="match_parent"
> >  android:layout_height="match_parent"/>
> > ```
> >
> > <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320165148269.png" alt="image-20220320165148269" style="zoom: 67%;float:left" />
> >
> > 果然，我的猜测应该是合理的

##### 4、自定义 View 和 ViewGroup 在绘制中的不同

> 在自定义 ViewGroup 中，一般不会重写 `onDraw()`，因为 ViewGroup 对 `onDraw()` 进行了处理，只有在**有背景图时**，才会调用 `onDraw()`，所以在自定义 ViewGroup 时想绘图一般是重写 `dispatchDraw(Canvas canvas)`，这个方法也可以决定是绘制在子 View 上层还是子 View 下层，具体的你们自己去实践一下吧，这里给出文章链接：https://www.jianshu.com/p/89efaf8bd3dd

#### 5、onDraw() 的绘制流程

> 这东西属于高阶内容，限于时间关系，这里我就不讲了，可以看看这些文章：
>
> - https://blog.csdn.net/carson_ho/article/details/56011153
> - https://github.com/Idtk/Blog/blob/master/Blog/9%E3%80%81Invalidate.md
>
> 

### 5、invalidate()

> 这东西你目前只需要记住以下几点就可以了：
>
> - 调用后会在**下一帧回调** `onDraw()` 进行刷新
> - 下一帧是指下一次屏幕刷新的时候
>
> 如果你想探究源码的话，我只能丢文章出来了：
>
> - https://juejin.cn/post/7017452765672636446
>
> OK，这个方法讲完了，因为往深了讲，会涉及到 Handler 机制、ViewRootImpl 等 Framwork 层源码
>
> 哦，想起了，还要一点，除了 `invalidate()` 外还有一个 `postInvalidate()` 用于在其他线程里刷新
>
> 但考虑到可能会有其他学长来听课，我还是画一个简单的流程图
>
> ```mermaid
> graph TB
> id1("View")-->id2
> id2["调用invalidate()刷新"]-->id3
> id3{"parent == null"}--不为 null-->id4
> id4["告诉父布局有子布局要重绘"]-->id3--为 null-->id5
> id5["传递到了 ViewRootImpl (一个管理布局的类)"]-->id6
> id6["ViewRootImpl 调用 mChoreographer 发送一个 post (一个专门监听屏幕刷新的类)"]-.->id7
> id7["屏幕刷新了，回调 ViewRootImpl，开始重新走 View 测量和绘图流程"]-->id8
> id8("从顶部布局最后回调到 View 的 onDraw()")
> ```
>
> 其实还有亿点细节，这里我就不讲述了，自己看下面图，其实这图仍有许多没有画上
>
> ```mermaid
> sequenceDiagram
> View->>View: invalidate()
> View->>ViewGroup: invalidateChild()
> alt 如果是硬件加速
> 	ViewGroup->>other ViewGroups: onDescendantInvalidated()
> 	other ViewGroups->>DecorView: onDescendantInvalidated()
> 	DecorView->>ViewRootImpl: onDescendantInvalidated()
> 	ViewRootImpl->>ViewRootImpl: invalidate()
> else 如果是软件加速
> 	ViewGroup->>other ViewGroups: invalidateChildInParent()
> 	other ViewGroups->>DecorView: invalidateChildInParent()
> 	DecorView->>ViewRootImpl: invalidateChildInParent()
> 	ViewRootImpl->>ViewRootImpl: invalidateRectOnScreen()
> end
> ViewRootImpl->>ViewRootImpl: scheduleTraversals()
> ViewRootImpl->>Choreographer: postCallback(mTraversalRunnable)
> Choreographer->>Handler: sendMessage*()
> Handler->>Handler: 等到下一次屏幕刷新
> Handler->>ViewRootImpl: mTraversalRunnable.run()
> ViewRootImpl->>ViewRootImpl: doTraversal()
> ViewRootImpl->>ViewRootImpl: performTraversals()
> ViewRootImpl->>ViewRootImpl: performDraw()
> ViewRootImpl->>DecorView: draw()
> DecorView->>other ViewGroups: draw()
> other ViewGroups->>ViewGroup: draw()
> ViewGroup->>View: draw()
> View->>View: onDraw()
> ```
>
> END

### 6、自定义属性 AttributeSet

> 前面我们提到系统会把你 xml 中写得属性装换为一个 AttributeSet 对象，那我们怎么自定义自己的属性呢？
>
> 首先，需要新建一个 `res/value/attrs.xml` 文件，比如我想要自定义一个圆半径的属性
>
> ```xml
> <resources>
>  <!--这个 name 是 View 的类名-->
>  <declare-styleable name="MySection6View">
>      <!--这个 name 是自定义属性的名称，formate 是接受的类型-->
>      <attr name="my6_circleRadius" format="dimension"/>
>  </declare-styleable>
> </resources>
> ```
>
> 如上述代码，这里有需要注意的一点：
>
> - 自定义属性的名称不能与官方的重复，而且建议加上前缀，与官方的进行区分，比如我就加上了 `my6_`
>
> `formate` 有下面几种类型：
>
> | 类型            | 说明     | 用法                          |                                        |
> | --------------- | -------- | ----------------------------- | -------------------------------------- |
> | ***dimension*** | 长度值   | "1dp"、"1px"、"@dimen/xxx"    | 这里无所谓 dp、px，系统会最终装换成 px |
> | ***reference*** | 引用值   | "@drawable/xxx"、"@style/xxx" |                                        |
> | ***color***     | 颜色值   | "#FFFFFFFF"、"@color/xxx"     |                                        |
> | ***float***     | 小数     | "1.0"                         |                                        |
> | ***boolean***   | 布尔类型 | "true"、"@bool/xxx"           |                                        |
> | ***interger***  | 整数     | "1"、"@interget/xxx"          |                                        |
> | ***string***    | 字符串   | "string"、"@string/xxx"       |                                        |
> | ***fraction***  | 百分数   | "50%"、"@fraction/xxx"        |                                        |
> | ***enum***      | 枚举     | [看下文](#enum)               |                                        |
> | ***flag***      | 位运算   | [看下文](#flag)               |                                        |
>
> ### enum
>
> > ```xml
> > <!--比如我设置圆的位置属性-->
> > <attr name="my6_circleGravity" format="enum">
> >     <enum name="left" value="0"/>
> >     <enum name="center" value="1"/>
> >     <enum name="right" value="2"/>
> > </attr>
> > ```
> >
> > <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220320195658810.png" alt="image-20220320195658810" style="zoom:50%;float:left" />
> >
> > 设置后在使用时它的属性就只能选择你填写的那几个
>
> ### flag
>
> > 就是进行位运算，这里我们以 FrameLayout 的 `layout_gravity` 为例
> >
> > ```xml
> > <attr name="layout_gravity">
> >     <!--0011 0000-->
> >     <flag name="top" value="0x30" />
> >     <!--0101 0000-->
> >     <flag name="bottom" value="0x50" />
> >     <!--0000 0011-->
> >     <flag name="left" value="0x03" />
> >     <!--0000 0101-->
> >     <flag name="right" value="0x05" />
> >     <!--0001 0000-->
> >     <flag name="center_vertical" value="0x10" />
> >     <!--0000 0001-->
> >     <flag name="center_horizontal" value="0x01" />
> >     <!--0001 0001-->
> >     <flag name="center" value="0x11" />
> >     <flag name="start" value="0x00800003" />
> >     <flag name="end" value="0x00800005" />
> > </attr>
> > ```
> >
> > 你是否还记得你曾经这样使用过？
> >
> > ```xml
> > <FrameLayout
> >     android:layout_width="match_parent"
> >     android:layout_height="match_parent">
> > 
> >     <View
> >         android:layout_width="match_parent"
> >         android:layout_height="match_parent"
> >         android:layout_gravity="top|left"/>
> > 
> > </FrameLayout>
> > ```
> >
> > > 这里你会发现它把 `top|left` 报黄了，原因在于它推荐你使用 `start` 去代替 `left`，因为在部分国家是从右边往左边阅读文字的，所以，Android 为了国际化，就设计出了 `start` 代替 `left`，`end` 代替 `right`
>
> ### 类型的混用
>
> > 比如 `android:layout_width` 是这样定义的：
> >
> > ```xml
> > <attr name="layout_width" format="dimension">
> >     <enum name="fill_parent" value="-1" />
> >     <enum name="match_parent" value="-1" />
> >     <enum name="wrap_content" value="-2" />
> > </attr>
> > ```
> >
> > 
> >
> > 还有 `android:background` 是这样定义的：
> >
> > ```xml
> > <attr name="background" format="reference|color" />
> > ```
> >
> > 所以你传入 `@drawable/xxx` 和 `#FFFFFFFF`，它都可以获取，只是填入颜色值时使用 `TypedValue#getDrawable()` 得到是 ColorDrawable
> >
> > 
> >
> > 也支持这样混用：
> >
> > ```xml
> > <!--这是 View 的一个属性-->
> > <attr name="focusable" format="boolean|enum">
> >     <enum name="auto" value="0x00000010" />
> > </attr>
> > ```
> >
> > 但这样使用只能手动去判断：
> >
> > ```java
> > private int getFocusableAttribute(TypedArray attributes) {
> >     TypedValue val = new TypedValue();
> >     if (attributes.getValue(com.android.internal.R.styleable.View_focusable, val)) {
> >         if (val.type == TypedValue.TYPE_INT_BOOLEAN) {
> >             return (val.data == 0 ? NOT_FOCUSABLE : FOCUSABLE);
> >         } else {
> >             return val.data;
> >         }
> >     } else {
> >         return FOCUSABLE_AUTO;
> >     }
> > }
> > ```
>
> 前面讲了类型的定义，那我们怎么获取这些属性呢？
>
> ```kotlin
> private var mCircleRadius = 100
> // 这个 enum 属性更建议使用枚举来保存，官方通常使用的注解，但 kt 很怪，注解不起作用
> private var mCircleGravity = 0
> init {
>     if (attrs != null) {
>         // 主要就是调用下面这个方法去获取属性
>         val ty = context.obtainStyledAttributes(attrs, R.styleable.MySection6View)
>         mCircleRadius = ty.getDimensionPixelSize(
>             R.styleable.MySection6View_my6_circleRadius,
>             mCircleRadius
>         )
>         mCircleGravity = ty.getInt(
>             R.styleable.MySection6View_my6_circleGravity,
>             mCircleGravity
>         )
>         ty.recycle()
>     }
> }
> ```
>
> 这里还好只有两个属性，但如果有多达上百个属性这样写未免太占行数了，所以，秉持着少写代码的原则，我使用了 kt 扩展函数来优化（下面是我写的课表自定义 View）：
>
> ```kotlin
> // 先把属性的读取单独提取到一个类中
> open class NetLayout @JvmOverloads constructor(
>     context: Context,
>     attrs: AttributeSet? = null,
>     defStyleAttr: Int = R.attr.netLayoutStyle,
>     defStyleRes: Int = 0
> ) : ViewGroup(context, attrs, defStyleAttr, defStyleRes), INetLayout {
>     // 属性值
>     protected val mNetAttrs: NetLayoutAttrs = NetLayoutAttrs.newInstance(this, attrs)
> }
> ```
>
> ````kotlin
> // 我把属性单独写在了一个类里面
> open class NetLayoutAttrs(
>     rowCount: Int,
>     columnCount: Int
> ) : BaseViewAttrs {
> 
>     var rowCount = rowCount
>         internal set
>     var columnCount = columnCount
>         internal set
> 
>     companion object {
>         fun newInstance(
>             view: View,
>             attrs: AttributeSet?,
>             defStyleAttr: Int = 0,
>             defStyleRes: Int = 0,
>         ): NetLayoutAttrs {
>             return newAttrs(
>                 view,
>                 attrs,
>                 R.styleable.NetLayout,
>                 defStyleAttr,
>                 defStyleRes
>             ) {
>                 // 这里你就会看到直接使用 R.styleable.xxx.int() 来获取属性，这样写起来舒服多了
>                 NetLayoutAttrs(
>                     R.styleable.NetLayout_net_rowCount.int(ROW_COUNT),
>                     R.styleable.NetLayout_net_columnCount.int(COLUMN_COUNT)
>                 )
>             }
>         }
> 
>         const val ROW_COUNT = 4
>         const val COLUMN_COUNT = 4
>     }
> }
> ````
>
> ```kotlin
> // 这是定义扩展函数的接口
> internal interface BaseViewAttrs {
> 
>     fun <T> newAttrs(
>         view: View,
>         attrs: AttributeSet?,
>         @StyleableRes
>         styleableId: IntArray,
>         defStyleAttr: Int = 0,
>         defStyleRes: Int = 0,
>         func: Typedef.() -> T
>     ): T = BaseViewAttrs.newAttrs(view, attrs, styleableId, defStyleAttr, defStyleRes, func)
> 
>     companion object {
>         fun <T> newAttrs(
>             view: View,
>             attrs: AttributeSet?,
>             @StyleableRes
>             styleableId: IntArray,
>             defStyleAttr: Int = 0,
>             defStyleRes: Int = 0,
>             func: Typedef.() -> T
>         ): T {
>             val ty = view.context.obtainStyledAttributes(attrs, styleableId, defStyleAttr, defStyleRes)
>             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
>                 // 这是保存在 Debug 模式中能看到的信息，具体怎么查看，你可以去看看这个方法的源码
>                 view.saveAttributeDataForStyleable(
>                     view.context, styleableId, attrs, ty, defStyleAttr, defStyleRes)
>             }
>             try {
>                 return Typedef(ty, view.context).func()
>             } finally {
>                 ty.recycle()
>             }
>         }
>     }
> 
>     class Typedef(val ty: TypedArray, private val context: Context) {
>         fun Int.int(defValue: Int): Int = this.int(ty, defValue)
>         fun Int.color(defValue: Int): Int = this.color(ty, defValue)
>         fun Int.colorById(@ColorRes defValueId: Int): Int = this.color(
>             ContextCompat.getColor(context, defValueId))
>         fun Int.dimens(defValue: Int): Int = this.dimens(ty, defValue)
>         fun Int.dimens(defValue: Float): Float = this.dimens(ty, defValue)
>         fun Int.layoutDimens(defValue: Int): Int = this.layoutDimens(ty, defValue)
>         fun Int.dimensById(@DimenRes defValueId: Int): Int = this
>         .dimens(context.resources.getDimensionPixelSize(defValueId))
>         fun Int.string(defValue: String? = null): String = this.string(ty, defValue)
>         fun Int.boolean(defValue: Boolean): Boolean = this.boolean(ty, defValue)
>         fun Int.float(defValue: Float): Float = this.float(ty, defValue)
>         internal inline fun <reified E: RuntimeException> Int.intOrThrow(
>             attrsName: String): Int = this.intOrThrow<E>(ty, attrsName)
>         internal inline fun <reified E: RuntimeException> Int.stringOrThrow(
>             attrsName: String): String = this.stringOrThrow<E>(ty, attrsName)
>     }
> }
> 
> internal fun Int.int(ty: TypedArray, defValue: Int): Int {
>     return ty.getInt(this, defValue)
> }
> 
> internal fun Int.color(ty: TypedArray, defValue: Int): Int {
>     return ty.getColor(this, defValue)
> }
> 
> internal fun Int.dimens(ty: TypedArray, defValue: Int): Int {
>     return ty.getDimensionPixelSize(this, defValue)
> }
> 
> internal fun Int.dimens(ty: TypedArray, defValue: Float): Float {
>     return ty.getDimension(this, defValue)
> }
> 
> internal fun Int.layoutDimens(ty: TypedArray, defValue: Int): Int {
>     return ty.getLayoutDimension(this, defValue)
> }
> 
> internal fun Int.string(ty: TypedArray, defValue: String? = null): String {
>     return ty.getString(this) ?: defValue ?: ""
> }
> 
> internal fun Int.boolean(ty: TypedArray, defValue: Boolean): Boolean {
>     return ty.getBoolean(this, defValue)
> }
> 
> internal fun Int.float(ty: TypedArray, defValue: Float): Float {
>     return ty.getFloat(this, defValue)
> }
> 
> internal inline fun <reified E: RuntimeException> Int.intOrThrow(
>     ty: TypedArray, attrsName: String
> ): Int {
>     if (!ty.hasValue(this)) {
>         throw E::class.java.getConstructor(String::class.java)
>             .newInstance("属性 $attrsName 没有被定义！")
>     }
>     return this.int(ty, 0)
> }
> 
> internal inline fun <reified E: java.lang.RuntimeException> Int.stringOrThrow(
>     ty: TypedArray, attrsName: String
> ): String {
>     if (!ty.hasValue(this)) {
>         throw E::class.java.getConstructor(String::class.java)
>             .newInstance("属性 $attrsName 没有被定义！")
>     }
>     return this.string(ty)
> }
> ```
>
> ### 自定义 layout_ 属性
>
> 之前在我给大家上的 Android 第一节课讲过，父 View 是可以给子 View 额外添加属性的，且属性名以 `layout_` 开头
>
> ```xml
> <!--比如说这个里面所有 layout_ 开头的都是父 View 给子 View 添加的额外属性-->
> <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
>     android:layout_width="match_parent"
>     android:layout_height="match_parent"
>     xmlns:app="http://schemas.android.com/apk/res-auto">
> 
>     <FrameLayout
>         android:layout_width="match_parent"
>         android:layout_height="match_parent"
>         app:layout_constraintEnd_toEndOf="parent"
>         app:layout_constraintStart_toStartOf="parent"
>         app:layout_constraintTop_toTopOf="parent">
> 
>         <View
>             android:layout_width="match_parent"
>             android:layout_height="match_parent"
>             android:layout_gravity="top|left" />
> 
>     </FrameLayout>
> </androidx.constraintlayout.widget.ConstraintLayout>
> ```
>
> 那如何自定义 ViewGroup 如何设置自己想要的属性？
>
> 比如我给它添加一个表示子 View 位置的属性：
>
> ```xml
> <resources>
>     <!--注意：这种属性的设置必须以 _Layout 结尾，这是官方的规范！-->
>     <declare-styleable name="My6ViewGroup_Layout">
>         <!--注意：属性名建议带有 layout_ 开头的标识-->
>         <attr name="my6VG_layout_position" format="integer"/>
>     </declare-styleable>
> </resources>
> ```
>
> 然后在 xml 中写得时候，AS 会进行智能提示
>
> <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321194906364.png" alt="image-20220321194906364" style="zoom:67%;" />
>
> 那怎么获取这个属性呢？
>
> 这个我们留到 [LayoutParams](#1、LayoutParams) 再讲解

### 7、onMeasure()

> ### 作用
>
> 测量自身和子控件大小
>
> ### 涉及知识
>
> - `MeasureSpecs`
> - `LayoutParams`
>
> ```kotlin
> // 与上面讲的 onDraw() 一样，也是一个回调，该回调的作用是由 requestLayout() 触发
> override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
>     super.onMeasure(widthMeasureSpec, heightMeasureSpec)
> }
> ```
>
> ### 主要用法
>
> - 调用 `setMeasuredDimension()`，设置自身宽和高
> - 遍历子 View，再调用 `child.measure()`，设置子 View 的宽和高
>
> ```mermaid
> graph LR
> id1("ViewRootImpl 开始测量")-->id2
> id2["ViewGroup#mearsure()"]-->id3
> id3["ViewGroup#onMeasure()"]-->id4
> id4["View#measure()"]-->id5("View#onMeasure()")
> ```
>
> 

#### 1、LayoutParams

> 这就是一个很简单的数据类，用于在子 View 中保存父类需要的信息
>
> 我们直接来看一下 FrameLayout 的 LayoutParams
>
> ```java
> // 你会发现这 LayoutParams 很简单
> public static class LayoutParams extends MarginLayoutParams {
>  /**
>      * 对应 layout_gravity 属性没有被设置
>      */
>     public static final int UNSPECIFIED_GRAVITY = -1;
> 
>     /**
>      * 这就是 layout_gravity 属性的保存值
>      * @see android.view.Gravity
>      * @attr ref android.R.styleable#FrameLayout_Layout_layout_gravity
>      */
>     public int gravity = UNSPECIFIED_GRAVITY;
> 
>     public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
>         super(c, attrs);
>         // 看到这里你应该就能回答之前留下的问题了
>         // 父 View 读取子 View 中的属性是在 LayoutParams 中获取的
>         // 可能你会奇怪于 View 是什么时候开始加载 LayoutParams
>         // 这个留到后面讲 setContentView 再讲解
>         final TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
>         gravity = a.getInt(R.styleable.FrameLayout_Layout_layout_gravity, UNSPECIFIED_GRAVITY);
>         a.recycle();
>     }
> 
>     public LayoutParams(int width, int height) {
>         super(width, height);
>     }
> 
>     public LayoutParams(int width, int height, int gravity) {
>         super(width, height);
>         this.gravity = gravity;
>     }
> 
>     public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
>         super(source);
>     }
> 
>     public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
>         super(source);
>     }
> 
>     public LayoutParams(@NonNull LayoutParams source) {
>         super(source);
>         this.gravity = source.gravity;
>     }
> }
> ```
>
> 如果要想使用这个 `LayoutParams` 还得重写这几个方法：
>
> ```kotlin
> // 这个是检查 LayoutParams 是否是你想要的 LayoutParams
> // 注意：这个 LayoutParams 是需要打个 ? 的，因为存在传入一个 null 的情况
> override fun checkLayoutParams(p: LayoutParams?): Boolean {
>     // 这是惯用写法
>     return p is NetLayoutParams
> }
> 
> // 这个是通过 AttributeSet 得到你自己的 LayoutParmas
> override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
>     return NetLayoutParams(context, attrs)
> }
> 
> // 这个是装换 LayoutParms
> override fun generateLayoutParams(lp: LayoutParams): LayoutParams {
>     // 这也是惯用写法，注意：要把你 LayoutParams 的所有父类都要写完
>     return when (lp) {
>         is NetLayoutParams -> NetLayoutParams(lp)
>         is MarginLayoutParams -> NetLayoutParams(lp)
>         else -> NetLayoutParams(lp)
>     }
> }
> 
> // 这个是得到默认的 LayoutParams
> override fun generateDefaultLayoutParams(): LayoutParams {
>     return NetLayoutParams(
>         NetLayoutParams.UNSET,
>         NetLayoutParams.UNSET,
>         NetLayoutParams.UNSET,
>         NetLayoutParams.UNSET
>     )
> }
> ```
>
> 这几个方法会在什么时候被调用，我们会在后面的 [LayoutInflater](#1、LayoutInflater) 讲解

#### 2、MeasureSpecs

> 推荐文章：https://www.jianshu.com/p/1260a98a09e9
>
> 它保存了测量模式和测量大小
>
> 比如上面代码中的：`widthMeasureSpec` 和 `heightMeasureSpec`
>
> 可能你会比较疑惑它是怎么用一个 int 来保存的，其实原理很简单，它使用高 2 位保存测量模式，后 30 为测量大小
>
> ```kotlin
> // 使用方式，原理就是位运算
> val wSize = MeasureSpec.getSize(widthMeasureSpec)
> val wMode = MeasureSpec.getMode(widthMeasureSpec)
> ```
>
> 测量模式有以下三种：
>
> | 模式              | 说明       | 应用场景                                     |
> | ----------------- | ---------- | -------------------------------------------- |
> | ***EXACTLY***     | 有具体值时 | 一般对应 `match_parent` 和具体的值           |
> | ***AT_MOST***     | 自适应大小 | 一般对应 `wrap_content`                      |
> | ***UNSPECIFIED*** | 可任意取值 | 只出现于 `ScrollView`、`ListView` 这类控件中 |
>
> 重写 ViewGroup 时就是通过这些来判断子 View 应该取得的高度的
>
> 关于怎么取，可以看这个在 ViewGroup 中的方法，里面是常见情况时的取法，该方法在很多官方控件中被使用
>
> ```java
> /**
>  * @param spec 子 View 能得到的 MeasureSpecs，一般是父 View 的测量模式 + 想给出的大小
>  * @param padding 间距值，一般这样填入：layoutParams.leftMargin + layoutParams.rightMargin
>  * @param childDimension 子 View 的宽或者高，固定填入：child.width 或者 child.height
>  */
> public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
>     int specMode = MeasureSpec.getMode(spec); // 测量模式
>     int specSize = MeasureSpec.getSize(spec); // 测量长度
> 
>     int size = Math.max(0, specSize - padding); // 先减去间距值得到的最大长度
> 
>     int resultSize = 0; // 保存最后的长度值
>     int resultMode = 0; // 保存最后的测量，模式
> 
>     switch (specMode) {
>     // 如果测量模式是具体值，即一般对应父 View 为 match_parent 或者 确定值
>     case MeasureSpec.EXACTLY:
>         if (childDimension >= 0) {
>             // 假设子 View 固定为 100dp
>             resultSize = childDimension;
>             resultMode = MeasureSpec.EXACTLY;
>         } else if (childDimension == LayoutParams.MATCH_PARENT) {
>             // 子 View 是 match_parent
>             resultSize = size; // 那结果值就是最大值
>             resultMode = MeasureSpec.EXACTLY; // 测量模式保持一样，为具体值
>         } else if (childDimension == LayoutParams.WRAP_CONTENT) {
>             // 子 View 是 wrap_content
>             resultSize = size;
>             resultMode = MeasureSpec.AT_MOST; // 改变测量模式为自适应大小
>         }
>         break;
> 
>     // 如果测量模式是自适应大小，即一般对应父 View 为 wrap_content
>     case MeasureSpec.AT_MOST:
>         if (childDimension >= 0) {
>             // 假设子 View 固定为 100dp
>             resultSize = childDimension;
>             resultMode = MeasureSpec.EXACTLY;
>         } else if (childDimension == LayoutParams.MATCH_PARENT) {
>             // 子 View 是 match_parent
>             // 这里比较重要
>             // 这里对应父 View 为 wrap_content，但子 View 却为 match_parent
>             resultSize = size; // 这里的意思是给出子 View 能得到的最大值
>             resultMode = MeasureSpec.AT_MOST;
>         } else if (childDimension == LayoutParams.WRAP_CONTENT) {
>             // 子 View 是 wrap_content
>             resultSize = size;
>             resultMode = MeasureSpec.AT_MOST;
>         }
>         break;
> 
>     // 如果测量模式是可任意取值，即一般对应父 View 为 ScrollView
>     case MeasureSpec.UNSPECIFIED:
>         if (childDimension >= 0) {
>             // 假设子 View 固定为 100dp
>             resultSize = childDimension;
>             resultMode = MeasureSpec.EXACTLY;
>         } else if (childDimension == LayoutParams.MATCH_PARENT) {
>             // 子 View 是 match_parent
>             // 这个 sUseZeroUnspecifiedMeasureSpec 变量用于兼容 Android 旧版本
>             resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
>             resultMode = MeasureSpec.UNSPECIFIED;
>         } else if (childDimension == LayoutParams.WRAP_CONTENT) {
>             // 子 View 是 wrap_content
>             resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
>             resultMode = MeasureSpec.UNSPECIFIED;
>         }
>         break;
>     }
>     return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
> }
> ```
>
> 如果你以后开发自定义 ViewGroup，在给子 View 测量时我更推荐使用该方法，遵循官方的写法可以提高可读性

#### 3、onMeasure() 源码相关分析

##### 1、FrameLayout 的 onMeasure() 源码分析

> ```java
> // 接下来是起飞环节
> @Override
> protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
>  int count = getChildCount();
> 
>  // 判断自身是否有一边不是具体值，作用如下：
>  // 比如 FrameLayout 的宽是 wrap_content，但其中一个子 View 宽为 match_parent
>  // 那么这个时候 FrameLayout 是不知道该给这个子 View 宽度多少的
>  // 但我们可以通过其他 View 来判断 FrameLayout 能得到的宽度
>  // 这个时候就知道 FraneLayout 宽度值能取多少了
>  final boolean measureMatchParentChildren =
>          MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
>          MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
>  // 这个是保存上述所说的那种子 View，用于二次测量
>  mMatchParentChildren.clear(); 
> 
>  int maxHeight = 0;
>  int maxWidth = 0;
>  // 状态值，用来记录子 View 是否得到了想要的大小，用的很少，忽略即可
>  int childState = 0; 
> 
>  // 遍历所有子 View
>  for (int i = 0; i < count; i++) {
>      final View child = getChildAt(i);
>      // 这个 mMeasureAllChildren 是一个属性，作用：是否忽略 Gone 的影响
>      // child.getVisibility() != GONE：Gone 时不测量子 View
>      // 这个 Gone 时再自己写自定义 View 建议也判断上
>      if (mMeasureAllChildren || child.getVisibility() != GONE) {
>          // measureChildWithMargins() 里面调用了测量子 View 的方法
>          // 之后会提到该方法，你只需要知道调用后可以得到子 View 测量的宽和高
>          // 但这个宽和高不是 width 和 height，而是 measuredWidth 和 measuredHeight
>          measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
>          // 得到 LayoutParams，主要是从里面取得定义的 Margin 值
>          final LayoutParams lp = (LayoutParams) child.getLayoutParams();
>          // 保存子 View 中最大的宽度
>          maxWidth = Math.max(maxWidth,
>                  child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
>          // 保存子 View 中最大的高度
>          maxHeight = Math.max(maxHeight,
>                  child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
>          // 状态值，忽略即可，在 ViewRootImpl 中有使用，一般情况下自定义 View 不用
>          childState = combineMeasuredStates(childState, child.getMeasuredState());
>          // 最开始的那个 boolean 值，用来保存是 match_parent 的 View
>          if (measureMatchParentChildren) {
>              if (lp.width == LayoutParams.MATCH_PARENT ||
>                      lp.height == LayoutParams.MATCH_PARENT) {
>                  mMatchParentChildren.add(child);
>              }
>          }
>      }
>  }
> 
>  // 最大值加上 padding 值
>  // getPadding*WithForeground() 是内部方法，我们自己用时是使用 getPadding*() 代替
>  maxWidth += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
>  maxHeight += getPaddingTopWithForeground() + getPaddingBottomWithForeground();
> 
>  // 检查最小值的设置
>  // View 都自带了一个 android:minHeight 属性，自己自定义 View 时建议把这个适配一下
>  maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
>  maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
> 
>  // 得到前台的背景图，再与最大值比较
>  final Drawable drawable = getForeground();
>  if (drawable != null) {
>      maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
>      maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
>  }
> 
>  // 关键方法，调用 setMeasuredDimension() 设置自身宽和高
>  setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
>          resolveSizeAndState(maxHeight, heightMeasureSpec,
>                  childState << MEASURED_HEIGHT_STATE_SHIFT));
> 
>  // 在上面那个调用过后，就能得到自身的宽和高了
>  // 然后这里再重写测量为 match_parent 的 View
>  count = mMatchParentChildren.size();
>  // 这个 count 判断我个人感觉有点问题
>  // count 是指为 match_parent View 的数量，
>  // 而如果我只有一个 View 是 match_parent，那它不是就不会重新测量了吗？
>  // 真奇怪，我感觉该用 childCount
>  if (count > 1) {
>      for (int i = 0; i < count; i++) {
>          final View child = mMatchParentChildren.get(i);
>          final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
> 
>          final int childWidthMeasureSpec;
>          if (lp.width == LayoutParams.MATCH_PARENT) {
>              final int width = Math.max(0, getMeasuredWidth()
>                         - getPaddingLeftWithForeground() - getPaddingRightWithForeground()
>                         - lp.leftMargin - lp.rightMargin);
>                 childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
>                         width, MeasureSpec.EXACTLY);
>             } else {
>                 childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
>                         getPaddingLeftWithForeground() + getPaddingRightWithForeground() +
>                         lp.leftMargin + lp.rightMargin,
>                         lp.width);
>             }
> 
>             final int childHeightMeasureSpec;
>             if (lp.height == LayoutParams.MATCH_PARENT) {
>                 final int height = Math.max(0, getMeasuredHeight()
>                         - getPaddingTopWithForeground() - getPaddingBottomWithForeground()
>                         - lp.topMargin - lp.bottomMargin);
>                 childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
>                         height, MeasureSpec.EXACTLY);
>             } else {
>                 childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
>                         getPaddingTopWithForeground() + getPaddingBottomWithForeground() +
>                         lp.topMargin + lp.bottomMargin,
>                         lp.height);
>             }
>             // 这里可以发现 onMeasure() 不止会调用一次，有时候需要调用多次才能测量完成 
>             child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
>         }
>     }
> 
> ```
>
> 

##### 2、NestedScrollView 嵌套 Rv 复用失效

> 接下来开始做火箭
>
> 我们来分析一下我寒假时多次提到 NestedScrollView 嵌套 Rv，导致 Rv 复用失效的问题，网上我还没有看过很详细的从源码角度来分析的文章
>
> 首先我们要想想问什么会失效？
>
> 失效说明 Rv 把全部子 View 都测量完了，那说明肯定是 `onMeasure()` 的问题，ok，那我们来 debug 一下 `onMeasure()` 的整个流程
>
> ##### 首先从 NestedScrollView 的 onMeasure() 开始 debug
>
> > ![image-20220321225743926](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321225743926.png)
> >
> > 可以发现 NestedScrollView 把测量直接交给了父类 FrameLayout 处理，（你可能会疑惑，不重写 `onMeasure()` 那 NestedScrollView 是怎么实现不同于其他 View 测量的？这个问题在下面会讲解）
> >
> > 又由于 `mFillViewport` 为 `false`，就直接 return 了，至于 `mFillViewport` 是什么我们后面会讲解
>
> ##### 来到 FrameLayout 的 onMeasure() 实现
>
> > ![image-20220321230004519](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321230004519.png)
> >
> > 这里只有一个 View，就是 Rv，然后 FrameLayout 调用了 `measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)`，这个函数被 NestedScrollView 重写了，然后给出了一个 `MeasureSpec.UNSPECIFIED`，这就是 NestedScrollView 不同于其他 View 测量的原因
> >
> > ![image-20220321230432739](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321230432739.png)
> >
> > 仔细看红线处，这里它给出的高度竟然直接为 `lp.topMargin + lp.bottomMargin`，可能你不会意识到这个有什么问题，我们来看看 NestedScrollView 的孪生兄弟 ScrollView 对于该方法的实现
> >
> > ![image-20220321230936516](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321230936516.png)
> >
> > 发现不同了吗，ScrollView 有个 `parentHeightMeasureSpec` 的高度，而 NestedScrollView 只使用了 `lp.topMargin + lp.bottomMargin`，如果子 View 没得 margin 值，那不就直接给子 View 传入的高度为 0 了？
> >
> > NestedScrollView 与 ScrollView 在这个方法上的不同，就是造成 Rv 复用失效的直接原因，接下来我们探究一下根本原因
>
> ##### 来到 Rv 的 onMeasure
>
> > 紧接着上面继续 debug，我们来到了 Rv 的 `onMeasure()` 实现
> >
> > ![image-20220321231555746](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321231555746.png)
> >
> > 可以发现这里 `heightSpec` 为 0
>
> ##### 来到 dispatchLayoutStep2() 方法
>
> > ![image-20220321231735164](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321231735164.png)
> >
> > 前面那个 `dispatchLayoutStrp1()` 因为不处于 `State.STEP_START` 而跳过了
>
> ##### 发现 mLayout.onLayoutChildren()
>
> > ![image-20220321232502431](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321232502431.png)
> >
> > 整个 `dispatchLayoutChild2()` 一看就只有这个方法是用于布局的，debug 进去试试
>
> ##### 探索 onLayoutChild() 方法
>
> > ![image-20220321233030377](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321233030377.png)
> >
> > 前面有一堆方法，但其实 Rv 是调用这个 `fill()` 来给子 View 布局的
>
> ##### 探索 fill()
>
> > ![image-20220321233713916](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321233713916.png)
> >
> > 这个 while 很重要，就是依靠这个循环来测量子 View 的，虽然你应该看的很懵逼，但请记住这两个东西：
> >
> > `layoutState.mInfinite = true` 和 `remainingSpace = 0`，后面的 `layoutState.hasMore(state)` 是用于判断次数是否达到 ItemCount 的，可以不用管
> >
> > 这里就直接告诉你结论，就是因为这个 `layoutState.mInfinite = true` 导致它一直执行循环，然后一直到 `layoutState.hasMore(state) = false` 才结束，即把 ItemCount 个 item 都测量完了
> >
> > 很懵逼是不是，我们来看看如果使用 ScrollView 包裹运行到这里时会怎么样？
> >
> > ![image-20220321234733578](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321234733578.png)
> >
> > 看到区别了吧，使用 ScrollView 时会不一样，其中 `layoutState.mInfinite = false` 和 `remainingSpace = 1868` 
> >
> > 如果使用 ScrollView 继续往下走
> >
> > ![image-20220321234928364](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321234928364.png)
> >
> > 他会在这个地方减少 `remainingSpace` 的值，最后就可以使 while 循环提前退出了
>
> OK，基本上根本原因找到了，但为什么那个 `layoutState.mInfinite = true` 且 `remainingSpace = 0` 呢？
>
> 继续分析
>
> ##### 为什么 layoutState.mInfinite = true ?
>
> > ![image-20220321235408579](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321235408579.png)
> >
> > 点击 `layoutState.mInfinite`，我们可以发现 `mLayoutState.mInfinite` 在这里被赋值
> >
> > 点进去看看它赋的什么值
> >
> > ![image-20220321235709470](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321235709470.png)
> >
> > 第一个 `getMode() == View.MeasureSpec.UNSPECIFIED` 肯定是 `true`，因为外布局是 NestedScrollView 嘛，前面提到了它重写了 FrameLayout 的 `measureChildWithMargins()` 方法，给的子 View 的测量模式是 `MeasureSpec.UNSPECIFIED` ，第二个判断 `mOrientationHelper.getEnd() == 0`，根据 debug 可以得到值也为 `true`
> >
> > 继续跟踪 `mOrientationHelper.getEnd() == 0` 的原因
> >
> > ![image-20220322000124439](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322000124439.png)
> >
> > 可以发现它直接调用了 `mLayoutManger.getHeight()`，继续
> >
> > ![image-20220322000411111](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322000411111.png)
> >
> > `mHeight` 被修改的地方如下
> >
> > ![image-20220322000630000](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322000630000.png)
> >
> > 其中前面两个是 `setRecyclerView()` 是在 Rv 添加 Adapter 时设置的初始值，肯定不是我们要找的地方，那只能是 `setMeasureSpecs()` 了
> >
> > ![image-20220322000826091](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322000826091.png)
> >
> > 这里有一个 `mHeight = 0` 的操作，点击方法名称看看是谁调用了它
> >
> > ![image-20220322000941813](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322000941813.png)
> >
> > 运气真好，刚好回到了我们之前大的断点前，那只能说明就是在这里调用的，所以 `mHeight = 0`，导致前面的 `mOrientationHelper.getEnd() == 0` 为 `true`，最后导致 `layoutState.mInfinite` 为 `true` 了 
>
> ##### 为什么 remainingSpace = 0？
>
> > 如果你大胆猜测的话，应该能猜到肯定与 `mHeight` 有关系
> >
> > 先来到这里
> >
> > ![image-20220322001506870](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322001506870.png)
> >
> > 我们可以知道 `remainingSpace` 由 `layoutState.mAvailable + layoutState.mExtraFillSpace` 组成，其中通过查看注释可以知道跟 `layoutState.mExtraFillSpace` 没有关系，那就去寻找 `layoutState.mAvailable` 吧
> >
> > 可是 `layoutState.mAvailable` 被改变的地方有点多，不是很好定位，那我们可以试试给这个变量打上断点，重新走一下流程，接下来就是重新 debug 一遍
> >
> > ![image-20220322002054498](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322002054498.png)
> >
> > 一下子就找到了，不得不说 debug 确实很方便
> >
> > 点击去看一下 `mOrientationHelper.getEndAfterPadding()` 方法
> >
> > ![image-20220322002252433](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322002252433.png)
> >
> > 果然，跟最开始的猜测一样，它与 `mHeight` 有关系
> >
> > 那 `updateLayoutStateToFillEnd()` 是在什么时候调用的呢？
> >
> > ![image-20220322002424079](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322002424079.png)
> >
> > 从调用栈发现原来他在 `fill()` 方法前被调用了
> >
> > OK，这下子 NestedScrollView 嵌套 Rv 使复用失效的根本原因和决定性因素都找到了
>
> ### 如何解决复用失效问题？
>
> 根据上面的流程，我们可以找到下面这几种方法：
>
> ##### 1、重写 NestedScrollView 的 `measureChildWithMargins()` 方法
>
> > ```kotlin
> > /**
> >  * 重写该方法的几个原因：
> >  * 1、为了在 UNSPECIFIED 模式下，Rv 也能得到 NestedScrollView 的高度
> >  * 2、NestedScrollView 与 ScrollView 在对于子 View 高度处理时在下面这个方法不一样, 导致
> >  *    NestedScrollView 中子 View 必须使用具体的高度, 设置成 wrap_content 或 match_parent
> >  *    都将无效，具体的可以去看 ScrollView 和 NestedScrollView 中对于这同一方法的源码
> >  * 3、在 NestedScrollView 中嵌套 RecyclerView 会使 RecyclerView 的懒加载失效，直接原因就与
> >  *    这个方法有关，而使用 ScrollView 就不会造成懒加载失效的情况
> >  * 4、至于为什么 NestedScrollView 与 ScrollView 在该方法不同，我猜测原因是为了兼容以前的 Android 版本，
> >  *    在 ViewGroup#getChildMeasureSpec() 方法中可以发现使用了
> >  *    一个静态变量 sUseZeroUnspecifiedMeasureSpec
> >  *    来判断 UNSPECIFIED 模式下子 View 该得到的大小，但可能设计 NestedScrollView “偷懒”了，
> >  *    没有加这个东西，具体原因不知
> >  */
> > override fun measureChildWithMargins(
> >     child: View,
> >     parentWidthMeasureSpec: Int,
> >     widthUsed: Int,
> >     parentHeightMeasureSpec: Int,
> >     heightUsed: Int
> > ) {
> >     val lp = child.layoutParams as MarginLayoutParams
> > 
> >     val childWidthMeasureSpec = getChildMeasureSpec(
> >         parentWidthMeasureSpec,
> >         paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin
> >                 + widthUsed, lp.width
> >     )
> >     // 这里的写法与 ScrollView 里面的一样
> >     val usedTotal = paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin + heightUsed
> >     val childHeightMeasureSpec: Int = MeasureSpec.makeMeasureSpec(
> >         max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotal),
> >         MeasureSpec.UNSPECIFIED
> >     )
> > 
> >     child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
> > }
> > ```
>
> ##### 2、直接给 Rv 设置固定高度
>
> > 这个方法就可以直接修改 `mHeight` 的值，从之前的分析中可以得到`layoutState.mInfinite` 和 `remainingSpace` 都与 `mHeight` 有关，所以修改 `mHeight` 就可以从根源上解决复用失效问题
>
> ##### 3、使用 mFillViewport 属性，但需要 NsetedScrollView 的 layout_height = match_parent 或 确定值
>
> > 前面在 `onMeasure()` 提到了一个 `mFillViewport` 变量
> >
> > 这是它的官方解释：
> >
> > > 设置此 ScrollView 是否应拉伸其内容高度以填充视口
> >
> > 它对应这个属性：
> >
> > ![image-20220322003418726](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322003418726.png)
> >
> > 但再次之前我们先看一下，设置为 `true` 后 `onMeasure()` 干了些什么东西
> >
> > ```java
> > @Override
> > protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
> >     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
> >     // mFillViewport 为 true 不会被 return 了
> >     if (!mFillViewport) {
> >         return;
> >     }
> > 
> >     final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
> >     if (heightMode == MeasureSpec.UNSPECIFIED) {
> >         return;
> >     }
> > 
> >     if (getChildCount() > 0) {
> >         View child = getChildAt(0);
> >         final NestedScrollView.LayoutParams lp = (LayoutParams) child.getLayoutParams();
> > 
> >         int childSize = child.getMeasuredHeight();
> >         
> >         // 关键在于这里，得到自身的测量高度
> >         // 这个测量高度是之前调用 super.onMeasure() 而设置的
> >         int parentSpace = getMeasuredHeight()
> >                 - getPaddingTop()
> >                 - getPaddingBottom()
> >                 - lp.topMargin
> >                 - lp.bottomMargin;
> > 
> >         if (childSize < parentSpace) {
> >             int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
> >                     getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
> >                     lp.width);
> >             // 给子 View 的测量模式使用了 MeasureSpec.EXACTLY
> >             int childHeightMeasureSpec =
> >                     MeasureSpec.makeMeasureSpec(parentSpace, MeasureSpec.EXACTLY);
> >             child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
> >         }
> >     }
> > }
> > ```
> >
> > 从上面代码我们可以发现，设置 `mFillViewport = true` 后 NestedScrollView 会使用 `MeasureSpec.EXACTLY` 模式再次测量子 View，高度使用的是自身的高度
> >
> > 而自身的高度只有在 `match_parent` 或者 确定值 时才有用，不然，如果你的 `layout_height` 为 `wrap_content`，那 `NestedScrollView#getMeasuredHeight()` 得到仍然是 Rv 全部测量时的高度，所以这时再测量还是会导致 Rv 复用失效
> >
> > 使用 mFillViewport 属性，但需要 NsetedScrollView 的 **layout_height = match_parent 或 确定值**
> >
> > 但一般都不会使用到这个属性来解决 Rv 复用失效问题，这里只是当个扩展来讲解
>
> ##### 问题：如果给 Rv 外面再包一层，那复用还会失效吗？
>
> > 答案：仍然会失效
> >
> > 首先，我们知道失效的决定因素是 `mHeight == 0 && mode == MeasureSpec.UNSPECIFIED`，而 NestedScrollView 在子 View 没有设置 Margin 值时给子 View 传入的高度肯定是 0
> >
> > ![image-20220321230936516](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220321230936516.png)
> >
> > 而 `MeasureSpec.UNSPECIFIED` 测量模式具有传递性，前面我们提到 `getChildMeasureSpec()` 方法（[MeasureSpecs](#2、MeasureSpecs)）
> >
> > ```java
> > // 如果测量模式是可任意取值，即一般对应父 View 为 ScrollView
> > case MeasureSpec.UNSPECIFIED:
> >     if (childDimension >= 0) {
> >         // 假设子 View 固定为 100dp
> >         resultSize = childDimension;
> >         resultMode = MeasureSpec.EXACTLY;
> >     } else if (childDimension == LayoutParams.MATCH_PARENT) {
> >         // 子 View 是 match_parent
> >         // 这个 sUseZeroUnspecifiedMeasureSpec 变量用于兼容 Android 旧版本
> >         resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
> >         resultMode = MeasureSpec.UNSPECIFIED;
> >     } else if (childDimension == LayoutParams.WRAP_CONTENT) {
> >         // 子 View 是 wrap_content
> >         resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
> >         resultMode = MeasureSpec.UNSPECIFIED;
> >     }
> >     break;
> > ```
> >
> > 可以看到除了 `childDimension >= 0` 外其他情况测量模式都是 `MeasureSpec.UNSPECIFIED`，所以你中间夹一层其他布局是解决不了的

##### 3、Dialog 根布局设置宽高失效

> 这个东西之前在寒假期间讲过，但才发现当时讲的有些小问题：
>
> - 只有 DialogFragment 才会使外层布局的所有 `layout_` 属性失效，而 Dialog 则**一般**不会
>
> 其实也不是一般不会，主要是 DialogFragment 和 Dialog 在一个方法使用上的不同，所以为什么我最开始给那位学弟讲的<img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322180055610.png" alt="image-20220322180055610" style="zoom: 50%;" />
>
> 原来是我以为 dialog 能设置，那 DialogFragment 也能设置了，原来 DialogFragment 设置是失效的
>
> 这里直接先讲原因：
>
> DialogFragment 是使用 `setContentView(View view)` 来设置根布局的，而 Dialog 一般使用 `setContentView(int id)` 来设置根布局，这两个方法在底层的调用会有些不同
>
> 我们直接从 DialogFragment 开始 debug
>
> DialogFragment 是一个 Fragment，里面夹带了一个 dialog，根据 Fragment 常见的写法，先给 `onCreateView()` 打上 debug
>
> ![image-20220326185016280](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220326185016280.png)
>
> debug 进来发现一堆方法，但如果不了解这些东西的话，确实很难知道它把这个返回的 View 拿来干了什么，其实当时我在给那位学弟找这个问题答案的时候找了很久，最后是从 `View#setLayoutParams()` 方法入手，在一堆调用栈中发现了答案。这里为了省时间就直接按正向流程讲一遍吧
>
> ![image-20220322180533094](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322180533094.png)
>
> 这里它有一个 `LiveData` 通知观察者
>
> ![image-20220322180809792](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322180809792.png)
>
> DialogFragment 里面对它进行了观察，DialogFragment 是在 `onAttach()` 的时候开始进行观察的，怪不得我按正常流程走了半天也找不到问题 :( 
>
> 这里的观察者被通知时调用了 `mDialog.setContentView(view)`，点击它继续往下走
>
> ![image-20220322190910124](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322190910124.png)
>
> 可以发现这里它直接传入了自己的 LayoutParams，这就是为什么所有 `layout_` 属性全部失效的原因
>
> 那普通的 dialog 为什么不会失效呢？
>
> 主要原因是 dialog 一般是这样写的：
>
> ![image-20220322191150701](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322191150701.png)
>
> <img src="https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220322191527878.png" alt="image-20220322191527878" style="zoom: 50%;" />
>
> 可以看到它调用了
>
> ```java
>mLayoutInflater.inflate(layoutResID, mContentParent);
> ```
> 
> 而不是使用的 `setContenView(View view)`
>
> 至于 `LayoutInflater` 会在后面进行讲解，这里你只需要知道调用了这个方法后，它会读取 xml 文件，并把 xml 中写的 `layout_` 属性保存在一个 `LayoutParams` 中供父布局使用，所以这就是 DialogFragment 的 `layout_` 属性不会失效的原因，当然，只是一般不会失效，如果你非要在 dialog 中调用 `setContentView(View view)`，那肯定也是一样会失效的
>
> ### 解决方案
>
> ##### 1、在 DialogFragment 的根布局外面再包一层 FrameLayout
>
> > 这样包了一层后就可以让你自己的布局的 `layout_` 在 FrameLayout 下生效，FrameLayout 的 `layout_width`、`layout_height` 任意设置都可以，因为失效了，但我更推荐设置成 `wrap_content`，这样看起来逻辑要好一点
>
> ##### 2、通过代码设置宽和高
>
> > 可以设置根布局的宽和高
>>
> > ```java
> > // 只有在 onViewCreated() 回调里设置才有效
> > override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
> >     super.onViewCreated(view, savedInstanceState)
> >     // density 是 dp / px 的转换率，比如：我的手机转换率是 2.75，则 1dp 对应 2.75px
> >     val density = requireContext().resources.displayMetrics.density
> >     val lp = view.layoutParams
> >     lp.width = (density * 400).toInt()
> >     lp.height = (density * 300).toInt()
> >     // 这里只是修改了宽和高，因为 View 还没有被测量布局
> >     // 所以可以不用调用 view.layoutParams = lp 来刷新，在其他地方使用时是要通过这种方式才能刷新的！
> > }
> > ```
> >
> > 也可以设置 window 的宽和高
> >
> > ```java
> > // 只有在 onViewCreated() 回调里设置才有效
> > override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
> >     super.onViewCreated(view, savedInstanceState)
> >     // density 是 dp / px 的转换率，比如：我的手机转换率是 2.75，则 1dp 对应 2.75px
> >     val density = requireContext().resources.displayMetrics.density
> >     dialog?.window?.setLayout(
> >         (density * 400).toInt(),
> >         (density * 300).toInt()
> >     )
> > }
> > ```
> >
> > 这个 window 其实是 Android 里的 `PhoneWindow`，听名字就知道是一个管理手机窗口的类，调用这个 `setLayout()` 最后会重新给 `DectorView` 设置 `LayoutParams`，`DectorView` 是所有窗口的根布局
> 
> 上面两种解决方法我更推荐使用第一种，因为在 xml 中定义属性更好修改，不然在代码中修改宽和高，会给以后看代码的人带来疑惑
>
> ##### 
>
> OK，`onMeasure()` 基本上就讲到这里了
>
> 

### 8、onLayout()

> ### 作用
>
> 布局子控件
>
> ### 涉及知识
>
> - `measureWidth` 与 `meaasureHeigth`
>
> ```kotlin
> /**
> * 与上面讲的 onDraw() 一样，也是一个回调，该回调的作用是由 requestLayout() 触发
> * @param changed 与上次布局相比，是否发生改变
> */
> override fun onLayout(                                
>     changed: Boolean,                                 
>     left: Int, top: Int, right: Int, bottom: Int      
> ) {                                                   
>     super.onLayout(changed, left, top, right, bottom) 
> }                                                     
> ```
>
> ### 主要用法
>
> - 遍历子 View，再调用 `child.layout()`，摆放子 View
>
> - 一般是 ViewGroup 实现
>
>   之前提到了 `TextView` 作为 View 却重写了这个方法，其实它没有干什么，主要是重新设置了文字的位置和大小
>
>   为什么需要在这里重写设置呢？
>
>   原因：`onLayout()` 能拿到最终显示的宽度和高度，且一般情况下只会调用一次，所以在有特殊需要时可以在这里面来设置一些东西，比如：一些特殊的自定义 ViewGroup 始终是固定的大小，则可以不用重写 `onMeasure()`，而是在 `onLayout()` 中直接给子 View 调用 `measure()`、`layout()` 布局
>
> ```mermaid
> graph LR
> id1("ViewRootImpl 开始布局")-->id2
> id2["ViewGroup#layout()"]-->id3
> id3["ViewGroup#onLayout()"]-->id4
> id4["View#layout()"]-->id5("View#onLayout()")
> ```
>
> 

#### 1、measureWidth (measureHeight) 与 width (height)

> `measureWidth` 代表测量的宽度，其实就是 `onMeasure()` 中调用 `setMeasuredDimension()` 设置的宽度
>
> `width` 代表布局后的宽度，是由 `layout()` 中摆放后通过 `right - left` 得到的
>
> 一般 `measureWidth` 只用于 `layout()` 和 `onLayout()` 中，其他地方不应该使用它

#### 2、FrameLayout 的 onLayout() 源码分析

> ```java
> @Override
> protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
>     layoutChildren(left, top, right, bottom, false /* no force left gravity */);
> }
> void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
>     final int count = getChildCount();
>     // 计算子 View 能绘制的边界
>     final int parentLeft = getPaddingLeftWithForeground();
>     final int parentRight = right - left - getPaddingRightWithForeground();
>     final int parentTop = getPaddingTopWithForeground();
>     final int parentBottom = bottom - top - getPaddingBottomWithForeground();
>     for (int i = 0; i < count; i++) {
>         final View child = getChildAt(i);
>         // 子 View 为 Gone 时不布局，在自己设计自定义 View 时建议也进行判断
>         if (child.getVisibility() != GONE) {
>             // 取出子 View 身上的 LayoutParams
>             final LayoutParams lp = (LayoutParams) child.getLayoutParams();
>             // 得到之前测量的宽和高
>             final int width = child.getMeasuredWidth();
>             final int height = child.getMeasuredHeight();
>             int childLeft;
>             int childTop;
>             // 前面提到的 FrameLayout#LayoutParams
>             int gravity = lp.gravity;
>             if (gravity == -1) {
>                 gravity = DEFAULT_CHILD_GRAVITY;
>             }
>             final int layoutDirection = getLayoutDirection();
>             final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
>             final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;
>             // 下面是根据不同的 gravity 来布局，就是一些简单的计算
>             switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
>                 case Gravity.CENTER_HORIZONTAL:
>                     childLeft = parentLeft + (parentRight - parentLeft - width) / 2 +
>                     lp.leftMargin - lp.rightMargin;
>                     break;
>                 case Gravity.RIGHT:
>                     if (!forceLeftGravity) {
>                         childLeft = parentRight - width - lp.rightMargin;
>                         break;
>                     }
>                 case Gravity.LEFT:
>                 default:
>                     childLeft = parentLeft + lp.leftMargin;
>             }
>             switch (verticalGravity) {
>                 case Gravity.TOP:
>                     childTop = parentTop + lp.topMargin;
>                     break;
>                 case Gravity.CENTER_VERTICAL:
>                     childTop = parentTop + (parentBottom - parentTop - height) / 2 +
>                     lp.topMargin - lp.bottomMargin;
>                     break;
>                 case Gravity.BOTTOM:
>                     childTop = parentBottom - height - lp.bottomMargin;
>                     break;
>                 default:
>                     childTop = parentTop + lp.topMargin;
>             }
>             // 最后调用 child.layout()
>             child.layout(childLeft, childTop, childLeft + width, childTop + height);
>         }
>     }
> }
> ```

### 9、requestLayout()

> 这东西跟前面的 `invalidate()` 一样，你们只需要记住以下几点：
>
> - 调用后会在下一帧回调 `onMeasure()` 和 `onMeasure()` 进行重新测量和布局
> - 如果你的 View 大小发生改变，它还会调用 `onDraw()` 进行刷新
>
> 文章的话，与 `invalidate()` 的一样：https://juejin.cn/post/7017452765672636446
>
> ```mermaid
> graph TB
> id1("View")-->id2
> id2["调用requestLayout()重新布局"]-->id3
> id3{"parent == null"}--不为 null-->id4
> id4["告诉父布局有子布局要重布局"]-->id3--为 null-->id5
> id5["传递到了 ViewRootImpl (一个管理布局的类)"]-->id6
> id6["ViewRootImpl 调用 mChoreographer 发送一个 post (一个专门监听屏幕刷新的类)"]-.->id7
> id7["屏幕刷新了，回调 ViewRootImpl，开始重新走 View 测量和布局流程"]-->id8
> id8("从顶部布局最后回调到 View 的 onMeasure()、onLayout()")-->id9
> id9{"如果宽高改变"}--true-->id10("调用 onDraw() 回调")-->id11
> id9--false-->id11("结束")
> ```
>
> 



### 10、setContentView

> debug 走起，最后发现它调用了
>
> ![image-20220323223301814](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323223301814.png)
>
> 添加布局直接交给了 `LayoutInflater` 处理，那我们讲解一下 `LayoutInflater`

#### 1、LayoutInflater

> `LayoutInflater` 我们常用的就下面这个两个方法
>
> ```java
> public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
>     // 可以看到它直接调用了下面那个三个参数的
> 	return inflate(resource, root, root != null);
> }
> 
> /**
>  * @param resource 布局 id
>  * @param root 父布局
>  * @param attachToRoot 是否直接添加到父布局，如果为 true，在解析出 View 后会直接添加到 root 中
>  * @return 如果 attachToRoot 为 true，这返回 root，如果为 false，则返回 xml 中的根布局
>  */
> public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {}
> ```
>
> 继续往里面走，其中最主要的代码在这里：
>
> ![image-20220323224318637](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323224318637.png)
>
> 上面那个就是之前留下的问题（[LayoutParams](#1、LayoutParams)），在 `root != null` 通过 `AttributeSet` 得到你自己的 `LayoutParmas`，然后在 `!attchToRoot` 时调用 `setLayoutParams()`，里面会调用 `requestLayout()`进行重新布局
>
> 继续往下面看：
>
> ![image-20220323230405944](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323230405944.png)
>
> 这里在 `root != null && attachToRoot` 时调用 `root.addView()`，这就是使用 `attachToRoot` 的时候
>
> 然后在 `addView()` 里面就调用了之前重写的那两个方法：
>
> ![image-20220323230713647](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323230713647.png)
>
> 这就是 `LayoutInflater` 的简单分析了
>
> 之前有学弟问道 `LayoutInflater#inflate()` 与 `View#inflate()` 的区别，查看源码你就会发现其实 `View#inflate()` 就是调用的 `LayoutInflater#inflate()`
>
> ![image-20220323231300755](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323231300755.png)
>
> 还有学弟问过为什么 Rv 的 `onCreateView()` 使用像下面这样写不行
>
> ```java
> // 其实这个 parent 就是 Rv
> override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
>     return MyViewHolder(
>         // 这种写法就是下面这种写法
>         View.inflate(parent.context, R.layout.recycler_item, parent)
>     )
> }
> 
> override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
>     return MyViewHolder(
>         LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, true)
>     )
> }
> 
> // 上面这两种写法使用后会报错：
> // java.lang.IllegalStateException: 
> // ViewHolder views must not be attached when created. 
> // Ensure that you are not passing 'true' to the attachToRoot parameter 
> // of LayoutInflater.inflate(..., boolean attachToRoot)
> // 意思就是只能在 ViewHolder 开始使用时才能把 View 添加到 parent 中去
> ```
>
> 可能部分有人这样写过，发现 item 的布局无法设置大小，这个问题跟前面讲到的 dialog 根布局宽高失效有点类似
>
> ```java
> override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
>     return MyViewHolder(
>         // 第二个参数传入 null
>         LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, null)
>     )
> }
> ```
>
> 第二个参数传入 null 时，按照前面分析的流程，就不会给根布局设置 `LayoutParams`，那么在 Rv `addView()`时，会调用 Rv 的 `generateDefaultLayoutParams()` ，最后调用到 `LayoutManger` 的 `generateDefaultLayoutParams()`
>
> ![image-20220323234144858](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323234144858.png)
>
> 如果是 `LinearLayoutManger`，就直接设置成 `wrap_content` 了，所以在 xml 中写的宽和高根本就没有去读取
>
> ![image-20220323234305639](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220323234305639.png)

### 11、发布开源库

#### 1、创建模块

> 大型项目都会采用多模块开发，每个人只需要负责自己的模块，模块方面的知识我就不讲解了，这里主要是讲解如何创建自定义 View 的模块
>
> 首先 new 一个模块
>
> ![image-20220325141248013](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325141248013.png)
>
> 然后选着对应的模块
>
> ![image-20220325141438062](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325141438062.png)
>
> > 这里简单讲一下：
> >
> > 第一个 `Phone & Tablet` 也是创建模块，其实与这个 `Android Library` 主要是在 `build.gradle` 上的细微不同
> >
> > ##### 1、Phone & Tablet
> >
> > ```groovy
> > plugins {
> >     id 'com.android.application'
> > }
> > 
> > android {
> >     defaultConfig {
> >     	applicationId "com.ndhzs.myapplication"
> >     	minSdk 21
> > 		targetSdk 31
> >     	versionCode 1
> > 	    versionName "1.0"
> >     }
> > }
> > ```
> >
> > ##### 2、Android Library
> >
> > ```groovy
> > plugins {
> >     id 'com.android.library'
> > }
> > 
> > android {
> >     defaultConfig {
> >     	minSdk 21
> > 		targetSdk 31
> >     }
> > }
> > ```
> >
> > 不同之处主要就是上面两处，其他还有 `AndroidManifest.xml` 都是差不多的
>
> 创建好的模块会少 res 文件夹，自己新建即可，其他地方用法就跟平常写法都一样
>
> 在你的 app 模块中使用下面这种写法就可以刚建的模块
>
> ```groovy
> dependencies {
> 	implementation project(':lib')
> }
> ```
>
> 模块下也是允许再建子模块的，模块化需要学习 gradle 相关的知识，等你们进来接手掌邮了多看几个模块就无师自通了（透漏一下新消息，目前强神还在更新整个掌邮的 gradle，已经重构为 kts 了，估计等你们进来就可以体验协程了）

#### 2、发布到 jitPack

> 很多时候发布到开源库方便我们在其他项目中引用，接下来我们将讲解如何发布到 `jitPack`，为什么不发布到 `Maven Central`，因为 `Maven Central` 要申请文件，很麻烦，而 `jitPack` 直接傻瓜式操作一步到位
>
> ```groovy
> // 在 lib 的 build.gradle 中
> plugins {
> 	id 'maven-publish'
> }
> 
> android {
> 	publishing {
> 		singleVariant "release"
> 	}
> }
> 
> afterEvaluate {
> 	publishing {
> 		publications {
> 			release(MavenPublication) {
> 				from components.release
> 			}
> 		}
> 	}
> }
> ```
>
> **注意是在 lib 的 build.gradle 中，不是在 app 的 build.gradle**
>
> > **这里是原因的讲解，不想看的话可以跳过**
> >
> > 这是我摸索出来的**最新教程**，如果你去那些看那些博客，全是过时教程，这里给出摸索的过程：
> >
> > - 发布开源库的教程先根据 `jitPack` 官网来：https://docs.jitpack.io/android/
> >
> > - 然后看它官网给出的源码示例：https://github.com/jitpack/android-example/blob/master/library/build.gradle
> >
> > - 再参考谷歌给的示例：https://developer.android.google.cn/studio/build/maven-publish-plugin
> >
> > - 最后看看 gradle 官网每个方法的意思：
> >
> >   https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:tasks
> >
> >   https://docs.gradle.org/current/dsl/org.gradle.api.publish.maven.MavenPublication.html#org.gradle.api.publish.maven.MavenPublication
> >
> > 
> >
> > 这样基本上能实现了，但在打包时会报一个警告：
> >
> > ```
> > WARNING:Software Components will not be created automatically for Maven publishing from Android Gradle Plugin 8.0. To opt-in to the future behavior, set the Gradle property 
> > ```
> >
> > 最后上 stackoverflow 寻找（因为其他地方找不到）：
> >
> > https://stackoverflow.com/questions/71365373/software-components-will-not-be-created-automatically-for-maven-publishing-from
> >
> > 最最后在谷歌找到方法：
> >
> > https://developer.android.google.cn/studio/publish-library/configure-pub-variants
> >
> > https://developer.android.google.cn/reference/tools/gradle-api/7.1/com/android/build/api/dsl/LibraryPublishing
> >
> > 这里简单讲一下：
> >
> > ```groovy
> > android {
> > 	publishing {
> >          // 这个是会自动创建下面 publications 中的同名方法
> > 		singleVariant "release"
> > 
> >          // 比如这个就是下面那个 myRelease(MavenPublication)
> >          // singleVariant "myRelease"
> > 	}
> > 
> >     // 这是官方默认自带的东西，就与对不同的包定义不同的设置
> >     buildTypes {
> >         release {
> >         }
> >     }
> > }
> > 
> > afterEvaluate {
> > 	publishing {
> > 		publications {
> > 			release(MavenPublication) {
> >                  // 这个 components.release 中的 release 是 buildTypes 中的 release
> > 				from components.release
> > 			}
> > 
> >              // 这里这个 myRelease 就对应于上面写的那个 singleVariant "myRelease"
> >              // myRelease(MavenPublication) {
> > 			//     from components.release
> > 			// }
> > 		}
> > 	}
> > }
> > ```
> >
> > 如果实在看不懂的话就直接按照刚开始给出的抄上去即可，毕竟看懂需要有一定的 gradle 基础
>
> 然后在 github 上发一个 Releases
>
> ![image-20220325133455931](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325133455931.png)
>
> Release 发布好了后，打开 `jitPack` 
>
> ![image-20220325133728477](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325133728477.png)
>
> 这样一个正式包就成功发布出去了，引入的话下面有教程
>
> ![image-20220325134213260](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325134213260.png)
>
> > 小插曲：由于 gradle 到 7.0.0 版本以后，引入改位置了
> >
> > ```groovy
> > // 在 settings.gradle 里
> > dependencyResolutionManagement {
> >  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
> >  repositories {
> >      google()
> >      mavenCentral()
> >      maven { url = "https://jitpack.io" }
> >  }
> > }
> > ```
>
> 如果是自己平时开发中用到，每次都发 Releases 显得过于麻烦，`jitPack` 可以发布快照版本，何为快照，看完下面的教程你就懂了
>
> ![image-20220325135853586](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325135853586.png)
>
> 你会发现下面给出的引入写法没有带版本号
>
> ![image-20220325135808864](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325135808864.png)
>
> 这个 `SNAPSHOP` 就是快照的标识，带有快照的版本号在 build 时会自动去检查是否是最新版，以后只需要提交到 main 分支，然后发个快照版本就可以升级了
>
> 只有自己使用时更推荐发布快照

#### 3、发布到阿里的 Maven

> 就在要上课的昨天，艾神来问我怎么发布阿里的 Maven，他掉进坑里卡了一个晚上，于是我去试着解决了一下
>
> 果然够坑的，因为 gradle 升到 7.0.0 以后把一个插件给移除了，而阿里官网和其他教程都是过时的，花了一些时间才搞出来，这里就直接给最新的操作教程
>
> 首先，去阿里官网注册一下（好像是免费的）：https://packages.aliyun.com/maven
>
> 注册过后，他会给你两个仓库：
>
> ![image-20220326231305660](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220326231305660.png)
>
> 一个是用于发布稳定版的库，一个是发布快照版的库，上面 `jitPack` 中也介绍了快照
>
> 然后找到这个指南界面
>
> ![image-20220326231921252](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220326231921252.png)
>
> 这里面有一些基础设置，但这教程过时了，下面我给出最新的写法：
>
> ```groovy
> // settings.gradle
> dependencyResolutionManagement {
>     repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
>     repositories {
>         google()
>         mavenCentral()
>         // 这里添加阿里 maven 的仓库地址
>         maven {
>             url 'https://maven.aliyun.com/repository/public'
>         }
>     }
> }
> ```
>
> ```groovy
> // 这是单独写的一个 maven.gradle
> 
> // gradle 7之前使用的 maven 插件，7之后只能使用 maven-publish 了
> apply plugin: 'maven-publish'
> // 这个是引用我的密码等私密文件，建议把这些单独放在一个文件夹里，并在.gitignore上添加它
> apply from: "${rootDir}/secret.gradle"
> 
> afterEvaluate {
>     publishing {
>         publications {
>             release(MavenPublication) {
>                 from components.release
>                 // 版本号，如果末尾加上 -SNAPSHOT 说明是快照版本
>                 version = '0.0.1-SNAPSHOT'
>                 // 项目名称，通常为类库模块名称，也可以任意设置
>                 artifactId = 'example'
>                 // 唯一标识，通常为模块包名，也可以任意设置
>                 groupId = 'com.985892345'
>             }
>         }
> 
>         // 这下面的 aliMavenUsername、aliMavenPassword、releaseUrl、snapshotUrl
>         // 对应你自己的，在阿里官网那个指南里有
>         repositories {
>             maven {
>                 credentials {
>                     username aliMavenUsername
>                     password aliMavenPassword
>                 }
>                 url releaseUrl
>             }
>             maven {
>                 credentials {
>                     username aliMavenUsername
>                     password aliMavenPassword
>                 }
>                 url snapshotUrl
>             }
>         }
>     }
> }
> ```
>
> ```groovy
> // secret.gradle
> // 这个没学过 gradle 的话可能看不懂，这里不会讲解，你只管这样写就可以
> ext.aliMavenUsername = "你的账号"
> ext.aliMavenPassword = "你的密码"
> ext.releaseUrl = "你的稳定版本发布地址"
> ext.snapshotUrl = "你的快照版本发布地址"
> ```
>
> ```groovy
> // 在你要发布的项目里的 build.gradle
> apply from: "maven.gradle"
> 
> android {
>     // 这个东西在之前 jitPack 讲过，这里就不再讲了
>     publishing {
>         singleVariant "release"
>     }
> }
> ```
>
> **注意：**通常你要发布的项目是一个 `library`，而不是一个 `application`，因为一般 `library` 才是给别人使用的，而 `application` 是一个项目的入口，通常来依赖 `library`
>
> OK，这样就配置完了，刷新一下 gradle，再点击右上角的 gradle 标志
>
> ![image-20220326234300251](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220326234300251.png)
>
> 点击这个就可以直接运行发布到 maven 的 task
>
> <img src="D:/Typora/img/image-20220326234410761.png" alt="image-20220326234410761" style="zoom: 67%;" />
>
> > 可能你们有些人找不到这个选项，因为新版的 AS 把这个默认关闭了，我也搞不懂为啥，为了让大家使用命令行运行任务？
> >
> > 在这里进行设置，就能看得到了
> >
> > ![image-20220326234634838](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220326234634838.png)
> >
> > 

### 12、分享一些东西

#### 1、Material Design 官网

> 谁想写自定义 View 啊，你想写吗？正经人写什么自定义 View。
>
> 自定义 View 确实很难，东西又多又杂，在考虑写自定义 View 之前，请先搜索一下是否有别人已经造好的轮子，有轮子直接用，他不香吗？[狗头]，Material Design 官网就包含许多官方轮子
>
> 官网链接：https://material.io/
>
> 源码地址：https://github.com/material-components/material-components-android
>
> 示例下载：https://github.com/material-components/material-components-android/releases
>
> 这里面的都算官方控件，而且有很多，如果想实现某个功能时可以去看看是否已经有实现了的，他还专门写了一个实例 app，可以下下来看看，找到想要的再去看他的源码

#### 2、MotionLayout

> 这东西我都还没怎么深入学习，可以去看看掘金上的一些教程，这里我就不讲了 ）
>
> 官网：https://developer.android.google.cn/training/constraint-layout/motionlayout/examples?hl=zh_cn

#### 2、View#post()、posyDelay()、postOnAnimation()

> ##### 面试题：
>
> 如何在 `onCreate()` 中得到 View 的宽和高？
>
> 哈哈，这不是有手就行？
>
> ```kotlin
> override fun onCreate(savedInstanceState: Bundle?) {
> 	super.onCreate(savedInstanceState)
> 	setContentView(com.ndhzs.lib.R.layout.layout_section7)
>         
> 	val view = findViewById<View>(com.ndhzs.lib.R.id.myView7)
> 	val width = view.width
> 	val height = view.height
> }
> ```
>
> 然后面试官笑了笑，你就被刷了
>
> 原因：这种写法得到的宽和高结果是 0
>
> 为什么呢？
>
> 因为前面我们讲到，`View#width` 和 `View#height` 是在 `onLayout()` 中设置的，而 `onCreate()` 此时还没有开始布局，只是调用了 `setContentView()` 加载了布局，三大流程还没有开始走，真正开始走三大流程是在 `onResume()` 后，这里提供下面几种正确方法：
>
> > ##### 1、post()
> >
> > ```kotlin
> > val view = findViewById<View>(com.ndhzs.lib.R.id.myView7)
> > view.post {
> >     val width = view.width
> >     val height = view.height
> > }
> > ```
> >
> > 原理：post 是用 Handler 发一个 Message，而 View 对于自带的 post 做了特殊处理，只会在 View 被测量后才开始发 Message
> >
> > ##### 2、doOnLayout
> >
> > ```kotlin
> > val view = findViewById<View>(com.ndhzs.lib.R.id.myView7)
> > view.doOnLayout {
> >     val width = view.width
> >     val height = view.height
> > }
> > ```
> >
> > 原理：使用 View 的一个 `addOnLayoutChangeListener()` 方法，监听 View 的 `onLayout()` 回调
>
> ##### 再来个面试题：
>
> View 的 `post()` 原理是什么？`postDelayed()` 会造成内存泄漏吗？如果会该怎么处理？
>
> 答案：
>
> 1、View 的 `post()` 在没有添加到屏幕前会先保存 Runnable 在第一次 `performTraversals` 到来时执行，如果已经添加屏幕上，就直接交给 `mAttachInfo` 的 `mHanlder` 执行，至于这个 `mHandler`，是 `ViewRootImpl` 持有的一个 `ViewRootHandler` 对象
>
> 2、`postDelayed()` 会造成内存泄漏，因为它没主动删除你发送的 Runnable（其实 `post()` 理论上也会造成内存泄漏，但因为时间极短，约等于不泄漏）
>
> 3、处理的话，可以在重写 View 的 `onDetachedFromWindow()` 方法主动取消，或者封装一下成单独的一个类，使用 `addOnAttachStateChangeListener()` 专门来发 `postDelayed()`
>
> > 如果面试官真的刁难你，可能还会问 `View#post()` 在不同版本上的实现 (
> >
> > 这个你们自己看文章吧：https://juejin.cn/post/6844903521804877832
> >
> > 还有如何检测内存泄漏，这个还是得提一下，不然不提的话，可能都不会有人知道
> >
> > 直接使用：LeakCanary  https://github.com/square/leakcanary
> >
> > 用法很简单，只需一行代码即可
> >
> > ```groovy
> > dependencies {
> >   // debugImplementation because LeakCanary should only run in debug builds.
> >   debugImplementation 'com.squareup.leakcanary:leakcanary-android:xxx'
> > }
> > ```
> >
> > 然后它会自己生成一个多余的图标，在你应用出现内存泄漏时弹窗提醒
> >
> > ![image-20220325155039418](https://img-1307243988.cos.ap-chengdu.myqcloud.com/typora/image-20220325155039418.png)
> >
> > 内存泄露也是面试的点（
>
> ##### 最后一个面试题
>
> `postOnAnimation()` 是什么？通常用于什么时候？
>
> 不要被 Animation 迷惑了，认为这个是用来发送动画的，如果要讲这个，我们先从官方常用的一个方法讲起，如果你经常看官方控件源码，你会发现下面这个东西常出现
>
> ```java
> ViewCompat.postOnAnimation()
> ```
>
> 如果你去看一遍它的注释，就很容易理解了，就是将 Runnable 在手机的下一帧执行（前面说过刷新率 60 的手机每帧相隔 16 毫秒），而这个方法内部在 SDK 16 及以上就是调用的 `View#postOnAnimation()`
>
> 那这个方法的使用场景如何呢？
>
> 它常用于代替 `ValueAnimator` 动画（`ValueAnimator` 这类动画与 `View#postOnAnimation()` 内部实现是一样的），比如在 Rv 的惯性滑动中就会用到这个 `ViewCompat.postOnAnimation()`（下次事件分发时才讲）
>
> 所以你会经常看见这样使用：
>
> ```java
> private Runnable mRunnable = new Runnable() {
> 	@Override
> 	public void run() {
> 		// ......
> 		ViewCompat.postOnAnimation(View.this, this)
>          // 平时开发可以直接使用 View.postOnAnimation(View.this, this)
> 	}
> }
> // 这样就形成了一种隐式的递归，在每次屏幕刷新时调用 run()
> ```
>
> 

#### 3、View 的生命周期

> View 也有什么周期，但 View 的生命周期很不完善
>
> ```mermaid
> graph TB
> id1("构造函数（对应 onCreate()）")-->id2
> id2["onFinishInflate()（对应 onCreate() 中的 setContentView()）"]-->id3
> id3["onAttachedToWindow()"]-->id4
> id4["onWindowVisbilityChanged()"]-->id5
> id5["onMeasure()"]-->id6
> id6["onLayout()"]-->id7
> id7["onDraw()"]-->id8
> id8["onWindowFocusChanged()"]-->id9
> id9["onWindowVisibilityChanged()"]-->id10
> id10["onDetachedFromWindow()"]
> ```
>
> ##### onFinishInflate()
>
>  从 xml 中完全加载完时的回调
>
> ##### onAttachedToWindow()
>
> 开始显示在屏幕上时的回调
>
> ##### onWindowVisbilityChanged()
>
> 窗口可见性改变的回调
>
> ##### onWindowFocusChanged()
>
> 窗口获取焦点时的回调
>
> ##### onDetachedFromWindow()
>
> View 不显示在屏幕上时的回调
>
> 其他的看看这篇文章吧：https://www.jianshu.com/p/0a4cb44ce9d1
>
> 可以看到其实 View 的生命周期不是很完善，甚至连自身什么时候被添加进 ViewGroup 都没有方法回调，remove 也是一样，但如果想实现 addView 和 removeView 监听的话，查看源码后发现只能使用 `ViewGroup#setOnHierarchyChangeListener()`，意思是在 View 中通过给 parent 设置监听，但这个有个缺点，就是只能设置一次监听

#### 4、自定义 View 的一些规范

> - 应该尽量解耦，View 里面不建议包含 `网络请求`、`Rxjavaj`、`EventBus`、`Lifecycle 相关组件` 等，这些应该是主语应用层使用的，而不是在 View 里面使用，View 里面应该尽量使用 MVC 的模式来写
> - 可以分离一些职责出来，比如课表我就把事件和绘制通过设置监听的方式来分离，这样可以方便扩展，而不是全部塞在 View 里面
>
> ##### 分离 Draw 事件
>
> ```kotlin
> // 自定义绘图的监听
> private val mItemDecoration = ArrayList<ItemDecoration>(5)
> 
> final override fun dispatchDraw(canvas: Canvas) {
>     mItemDecoration.forEach {
>         it.onDrawBelow(canvas, this)
>     }
>     super.dispatchDraw(canvas)
>     mItemDecoration.forEach {
>         it.onDrawAbove(canvas, this)
>     }
> }
> 
> /**
>  * 该类主要用于实现一些简单的绘图处理
>  *
>  * 经过我的思考，我认为不应该提供删除的方法，原因如下：
>  * - 一般不会有需要中途删除的情况
>  * - 很容易出现事件在遍历中就把它删除，导致出现遍历越位的错误
>  *
>  * 设计参考了 RV 的 ItemDecoration
>  * @author 985892345 (Guo Xiangrui)
>  * @email 2767465918@qq.com
>  * @date 2022/1/27
>  */
> interface ItemDecoration {
>     /**
>      * 在所有子 View 的 onDraw() 前的回调，在这里面绘图可以绘制在子 View 下方
>      */
>     fun onDrawBelow(canvas: Canvas, view: View) { }
> 
>     /**
>      * 在所有子 View 的 onDraw() 后的回调，在这里面绘图可以绘制在子 View 上方
>      */
>     fun onDrawAbove(canvas: Canvas, view: View) { }
> }
> ```
>
> 还有分离事件分发，我就留到下次课来讲了
>
> 

#### 5、布局调试软件

> Android 调试利器 Pandora
>
> 一个很牛逼的 debug 工具，可以在手机上查看布局属性、网络请求、sp 等很多东西
>
> https://www.wanandroid.com/blog/show/2183

#### 6、Rv 怎么得到某个 item  的实例

> 这不算自定义 View 的内容，因为寒假时有很多学弟遇到了这个问题，我这里讲解一下吧
>
> 得到里面的 item 更建议使用在不需要保持状态的情况下，比如某个 item 的变动需要加载动画，那就可以使用这种方法
>
> ```kotlin
> class RvAdapter : RecyclerView.Adapter<RvAdapter.RvVH>() {
>  class RvVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
>      val mAnimationView: AnimationView = itemView.findViewById(R.id.item_animation)
>  }
> 
>  fun getVHolder(position: Int, call: VHolderCallback) {
>      // 调用两个参数的这个刷新
>      notifyItemChanged(position, call)
>  }
> 
>  override fun onCreateViewHolder(
>      parent: ViewGroup, 
>      viewType: Int
>  ): RvVH {
>      return RvVH(
>          LayoutInflater
>          	.from(parent.context)
>          	.inflate(R.layout.layout_item, parent, false)
>      )
>  }
> 
>  // 重写三个参数的这个方法
>  override fun onBindViewHolder(
>      holder: RvVH, 
>      position: Int, 
>      payloads: MutableList<Any>
>  ) {
>      if (payloads.isEmpty()) {
>          super.onBindViewHolder(holder, position, payloads)
>      } else {
>          // 这里得到 payloads 就是之前刷新传入的回调
>          // 因为是在下一帧才会刷新，期间可能会调用多次刷新 item，所以是一个 List
>          payloads.forEach {
>              if (it is VHolderCallback) {
>                  it.call(holder)
>              }
>          }
>      }
>  }
> 
>  override fun onBindViewHolder(holder: RvVH, position: Int) {
>  }
> 
>  override fun getItemCount(): Int = 100
> 
>  fun interface VHolderCallback {
>      fun call(holder: RvVH)
>  }
> }
> ```
>
> 使用方式就是调用 `getVHolder()` 就可以了
>
> ```kotlin
> getVHolder(1) {
>  // 开始动画
>  it.mAnimationView.startAnim()
> }
> ```
>
> **注意：**
>
> - 这种方法在回调时不能进行延迟保存返回的 holder 对象，因为它只是目前状态下的 holder，不能保证刷新后仍是这个
>
> - 这里面对 View 的设置都是暂时的，所以最开始我就说了用来调用一下动画才能使用这种方法，如果想永久保存建议：
>
>   - 改变传入 Rv 的数据集合，
>   - 或者调用 `ViewHolder#setIsRecyclable(false)`，但要记得在之后还原，不然这个 ViewHolder 就不会被回收
>
> - 这种情况下会失效：
>
>   ```kotlin
>   getVHolder(1) {
>       // 开始动画
>       it.setIsRecyclable(false)
>       it.mAnimationView.startAnim()
>   }
>   // 后面调用这个进行普通刷新，只要出现了这个，前面的特殊刷新都会失效
>   notifyItemChanged(1)
>   ```
>
> 对于差分刷新也有一个与 payload 相关的方法
>
> ```kotlin
> // 这个是 ItemCallback，但与 DiffUtil.Callback 是差不多的
> object : DiffUtil.ItemCallback<ICourseVpBean>() {
>     override fun areItemsTheSame(
>         oldItem: ICourseVpBean,
>         newItem: ICourseVpBean
>     ): Boolean {
>         return oldItem.week == newItem.week
>     }
> 
>     override fun areContentsTheSame(
>         oldItem: ICourseVpBean,
>         newItem: ICourseVpBean
>     ): Boolean {
>         return oldItem == newItem
>     }
> 
>     // 就这个方法，建议重写并不返回
>     override fun getChangePayload(
>         oldItem: ICourseVpBean, 
>         newItem: ICourseVpBean
>     ): Any {
>         return "" // 只要不为 null 就可以在刷新时去掉与缓存的互换，减少性能的消耗
>     }
> }
> ```

## 二、动画

> 动画这东西建议看《Android自定义控件开发入门与实战》和下面这网址，我这里就不进行讲解了
>
> https://qijian.blog.csdn.net/article/details/50995268
>
> 本节课主要讲元素共享动画
>
> 因为时间关系写不了了（这课件确实把我写累了），这里给出一些网址：
>
> - https://juejin.cn/post/6844903727015395336
>
>   > 这个主要写了 activity 之间的元素共享，例子较全
>
> - https://blog.csdn.net/qq_29425853/article/details/53104919
>
>   