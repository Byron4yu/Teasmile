<template>
  <div class="addDrink">
    <div class="leftCont">
      <div v-show="seachKey.trim() == ''"
           class="tabBut">
        <span v-for="(item, index) in DrinkType"
              :key="index"
              :class="{ act: index == keyInd }"
              @click="checkTypeHandle(index, item.id)">{{ item.name }}</span>
      </div>
      <div class="tabList">
        <div class="table"
             :class="{ borderNone: !DrinkList.length }">
          <div v-if="DrinkList.length == 0"
               style="padding-left: 10px">
            <Empty />
          </div>
          <el-checkbox-group v-if="DrinkList.length > 0"
                             v-model="checkedList"
                             @change="checkedListHandle">
            <div v-for="(item, index) in DrinkList"
                 :key="item.name + item.id"
                 class="items">
              <el-checkbox :key="index"
                           :label="item.name">
                <div class="item">
                  <span style="flex: 3; text-align: left">{{
                    item.DrinkName
                  }}</span>
                  <span>{{ item.status == 0 ? '停售' : '在售' }}</span>
                  <span>{{ (Number(item.price) ).toFixed(2)*100/100 }}</span>
                </div>
              </el-checkbox>
            </div>
          </el-checkbox-group>
        </div>
      </div>
    </div>
    <div class="ritCont">
      <div class="tit">
        已选饮品({{ checkedListAll.length }})
      </div>
      <div class="items">
        <div v-for="(item, ind) in checkedListAll"
             :key="ind"
             class="item">
          <span>{{ item.DrinkName || item.name }}</span>
          <span class="price">￥ {{ (Number(item.price) ).toFixed(2)*100/100 }} </span>
          <span class="del"
                @click="delCheck(item.name)">
            <img src="./../../../assets/icons/btn_clean@2x.png"
                 alt="">
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
// import {getDrinkTypeList, getDrinkListType} from '@/api/drink';
import { getCategoryList, queryDrinkList } from '@/api/drink'
import Empty from '@/components/Empty/index.vue'

@Component({
  name: 'selectInput',
  components: {
    Empty
  }
})
export default class extends Vue {
  @Prop({ default: '' }) private value!: number
  @Prop({ default: [] }) private checkList!: any[]
  @Prop({ default: '' }) private seachKey!: string
  private DrinkType: [] = []
  private DrinkList: [] = []
  private allDrinkList: any[] = []
  private DrinkListCache: any[] = []
  private keyInd = 0
  private searchValue: string = ''
  public checkedList: any[] = []
  private checkedListAll: any[] = []
  private ids: any = new Set()
  created() {
    this.init()
  }

  @Watch('seachKey')
  private seachKeyChange(value: any) {
    if (value.trim()) {
      this.getDrinkForName(this.seachKey)
    }
  }

  public init() {
    // 菜单列表数据获取
    this.getDrinkType()
    // 初始化选项
    this.checkedList = this.checkList.map((it: any) => it.name)
    // 已选项的饮品-详细信息
    this.checkedListAll = this.checkList.reverse()
  }
  // 获取套餐分类
  public getDrinkType() {
    getCategoryList({ type: 1 }).then(res => {
      if (res && res.data && res.data.code === 1) {
        this.DrinkType = res.data.data
        this.getDrinkList(res.data.data[0].id)
      } else {
        this.$message.error(res.data.msg)
      }
      // if (res.data.code == 200) {
      //   const { data } = res.data
      //   this.   = data
      //   this.getDrinkList(data[0].category_id)
      // } else {
      //   this.$message.error(res.data.desc)
      // }
    })
  }

  // 通过套餐ID获取饮品列表分类
  private getDrinkList(id: number) {
    queryDrinkList({ categoryId: id }).then(res => {
      if (res && res.data && res.data.code === 1) {
        if (res.data.data.length == 0) {
          this.DrinkList = []
          return
        }
        let newArr = res.data.data
        newArr.forEach((n: any) => {
          n.DrinkId = n.id
          n.copies = 1
          // n.DrinkCopies = 1
          n.DrinkName = n.name
        })
        this.DrinkList = newArr
        if (!this.ids.has(id)) {
          this.allDrinkList = [...this.allDrinkList, ...newArr]
        }
        this.ids.add(id)
      } else {
        this.$message.error(res.data.msg)
      }
    })
  }

  // 关键词收搜饮品列表分类
  private getDrinkForName(name: any) {
    queryDrinkList({ name }).then(res => {
      if (res && res.data && res.data.code === 1) {
        let newArr = res.data.data
        newArr.forEach((n: any) => {
          n.DrinkId = n.id
          n.DrinkName = n.name
        })
        this.DrinkList = newArr
      } else {
        this.$message.error(res.data.msg)
      }
    })
  }
  // 点击分类
  private checkTypeHandle(ind: number, id: any) {
    this.keyInd = ind
    this.getDrinkList(id)
  }
  // 添加饮品
  private checkedListHandle(value: [string]) {
    // TODO 实现倒序 由于value是组件内封装无法从前面添加 所有取巧处理倒序添加
    // 倒序展示 - 数据处理前反正 为正序
    this.checkedListAll.reverse()
    // value 是一个只包含饮品名的数组 需要从 DrinkList中筛选出 对应的详情
    // 操作添加饮品
    const list = this.allDrinkList.filter((item: any) => {
      let data
      value.forEach((it: any) => {
        if (item.name == it) {
          data = item
        }
      })
      return data
    })
    // 编辑的时候需要与已有饮品合并
    // 与当前请求下的选择性 然后去重就是当前的列表
    const DrinkListCat = [...this.checkedListAll, ...list]
    let arrData: any[] = []
    this.checkedListAll = DrinkListCat.filter((item: any) => {
      let allArrDate
      if (arrData.length == 0) {
        arrData.push(item.name)
        allArrDate = item
      } else {
        const st = arrData.some(it => item.name == it)
        if (!st) {
          arrData.push(item.name)
          allArrDate = item
        }
      }
      return allArrDate
    })
    // 如果是减菜 走这里
    if (value.length < arrData.length) {
      this.checkedListAll = this.checkedListAll.filter((item: any) => {
        if (value.some(it => it == item.name)) {
          return item
        }
      })
    }
    this.$emit('checkList', this.checkedListAll)
    // 数据处理完反转为倒序
    this.checkedListAll.reverse()
  }

  open(done: any) {
    this.DrinkListCache = JSON.parse(JSON.stringify(this.checkList))
  }

  close(done: any) {
    this.checkList = this.DrinkListCache
  }

  // 删除
  private delCheck(name: any) {
    const index = this.checkedList.findIndex(it => it === name)
    const indexAll = this.checkedListAll.findIndex(
      (it: any) => it.name === name
    )

    this.checkedList.splice(index, 1)
    this.checkedListAll.splice(indexAll, 1)
    this.$emit('checkList', this.checkedListAll)
  }
}
</script>
<style lang="scss">
.addDrink {
  .el-checkbox__label {
    width: 100%;
  }
  .empty-box {
    margin-top: 50px;
    margin-bottom: 0px;
  }
}
</style>
<style lang="scss" scoped>
.addDrink {
  padding: 0 20px;
  display: flex;
  line-height: 40px;
  .empty-box {
    img {
      width: 190px;
      height: 147px;
    }
  }

  .borderNone {
    border: none !important;
  }
  span,
  .tit {
    color: #333;
  }
  .leftCont {
    display: flex;
    border-right: solid 1px #efefef;
    width: 60%;
    padding: 15px;
    .tabBut {
      width: 110px;
      font-weight: bold;
      border-right: solid 2px #f4f4f4;
      span {
        display: block;
        text-align: center;
        // border-right: solid 2px #f4f4f4;
        cursor: pointer;
        position: relative;
      }
    }
    .act {
      border-color: $mine !important;
      color: $mine !important;
    }
    .act::after {
      content: ' ';
      display: inline-block;
      background-color: $mine;
      width: 2px;
      height: 40px;
      position: absolute;
      right: -2px;
    }
    .tabList {
      flex: 1;
      padding: 15px;
      height: 400px;
      overflow-y: scroll;
      .table {
        border: solid 1px #f4f4f4;
        border-bottom: solid 1px #f4f4f4;
        .items {
          border-bottom: solid 1px #f4f4f4;
          padding: 0 10px;
          display: flex;
          .el-checkbox,
          .el-checkbox__label {
            width: 100%;
          }
          .item {
            display: flex;
            padding-right: 20px;
            span {
              display: inline-block;
              text-align: center;
              flex: 1;
              font-weight: normal;
            }
          }
        }
      }
    }
  }
  .ritCont {
    width: 40%;
    .tit {
      margin: 0 15px;
      font-weight: bold;
    }
    .items {
      height: 338px;
      padding: 4px 15px;
      overflow: scroll;
    }
    .item {
      box-shadow: 0px 1px 4px 3px rgba(0, 0, 0, 0.03);
      display: flex;
      text-align: center;
      padding: 0 10px;
      margin-bottom: 20px;
      border-radius: 6px;
      color: #818693;
      span:first-child {
        text-align: left;
        color: #20232a;
        flex: 70%;
      }
      .price {
        display: inline-block;
        flex: 70%;
        text-align: left;
      }
      .del {
        cursor: pointer;
        img {
          position: relative;
          top: 5px;
          width: 20px;
        }
      }
    }
  }
}
</style>
