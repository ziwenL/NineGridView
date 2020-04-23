# NineGridView
<h3 align="center">基本介绍</h3>
<p align="center" blod=true >NineGridView是一个仿照 钉钉-发现-圈子 实现多图展示效果的自定view</p>
<h6>显示效果</h6>
<img src="https://github.com/ziwenL/NineGridView/blob/master/readme/images/examples.gif?raw=true" />

<h6>使用步骤</h6>
<ul>
<li>
<p>1.复制<a href="https://github.com/ziwenL/NineGridView/blob/master/app/src/main/java/com/ziwenl/ninegridview/widgets/NineGirdView.java"  rel="nofollow">NineGirdView</a>类到项目中，替换TODO标签处的图片加载框架，复制<a href="https://github.com/ziwenL/NineGridView/blob/master/app/src/main/res/values/attrs.xml"  rel="nofollow">attrs</a>中定义的自定义属性</p>
</li>
<li>
<p>2.在布局中引用NineGridView，宽度应为固定值或match_parent，高度应为wrap_content</p>
</li>
<li>
<p>3.通过NineGridView.setUrls(List<String> urls)方法填充网络图片</p>
</li>
<li>
<p>4.通过NineGridView.setCallback(NineGridView.Callback callback)方法获取图片点击回调</p>
</li>
</ul>

<h6>功能说明</h6>
<ul>
<li>
<p>该View继承于CardView以达到实现圆角的目的(也就是通过cardCornerRadius属性设置圆角值)</p>
</li>
<li>
<p>圆角及实现效果因为不依赖于图片加载框架实现，所以可自行选用替换图片加载框架</p>
</li>
<li>
<p>图片数量为一张时，按比例缩放图片，圆角不会随着图片宽高不同而变形</p>
</li>
<li>
<p>提供自定义属性pictureMaxHeight：图片数量只有一张时该图最大显示高度</p>
</li>
<li>
<p>提供自定义属性pictureSpace：图片之间的间隔距离</p>
</li>
<li>
<p>填充网络图片：setUrls(List<String> urls)</p>
</li>
<li>
<p>监听图片点击事件：setCallback(NineGridView.Callback callback)</p>
</li>
</ul>

<h6>注意事项</h6>
<ul>
<li>
<p>需在NineGridView中的TODO标注里自行替换当前项目所使用的图片加载框架(推荐使用Glide或Picasso，如使用Fresco需要自行调整源码)</p>
</li>
<li>
<p>该View中的ImageView，除了图片数量为1时，采用的是按比例自适应宽高(有最大高度及宽度限制)，其余情况下ImageView的宽高都是固定值。具体值由NineGridView能展示的最大宽度决定</p>
</li>
<li>
<p>该View用于RecyclerView时，为了解决单图item被回收后，重新出现在屏幕时由于高度变化造成item跳动的问题，需要牺牲部分recyclerView的复用特性。具体表现为重写适配器getItemViewType方法，判断图片数量是否为1，是则将position返回作为itemViewType<a href="https://github.com/ziwenL/NineGridView/blob/master/app/src/main/java/com/ziwenl/ninegridview/ui/adapter/MainAdapter.kt"  rel="nofollow">（写法参考）</a>(实则最优解应该是为单图情况专门写一个itemLayout并赋予指定itemViewType)</p>
</li>
<li>
<p>NineGridView使用Java实现，使用示例使用kotlin实现</p>
</li>
</ul>

<h3>About Me<h3>
<ul>
<li>
<p>Email: ziwen.lan@foxmail.com</p>
</li>
</ul>
