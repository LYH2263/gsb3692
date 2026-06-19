import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 引入 Vant 样式
import 'vant/lib/index.css'
import { 
  Button, 
  Tabbar, 
  TabbarItem, 
  Icon, 
  Search, 
  Tab, 
  Tabs, 
  List, 
  PullRefresh, 
  Image as VanImage, 
  Cell, 
  CellGroup, 
  Form, 
  Field, 
  Uploader,
  NavBar,
  Dialog,
  ActionSheet,
  Sticky,
  Swipe,
  SwipeItem,
  Lazyload
} from 'vant'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(Lazyload)

// 注册 Vant 组件
const vantComponents = [
  Button, 
  Tabbar, 
  TabbarItem, 
  Icon, 
  Search, 
  Tab, 
  Tabs, 
  List, 
  PullRefresh, 
  VanImage, 
  Cell, 
  CellGroup, 
  Form, 
  Field, 
  Uploader,
  NavBar,
  Dialog,
  ActionSheet,
  Sticky,
  Swipe,
  SwipeItem
]

vantComponents.forEach(component => {
  app.use(component)
})

app.mount('#app')
