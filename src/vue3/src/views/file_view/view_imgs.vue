<template>
  <div class="common-layout" :style="props.scrollAble ? 'height: 100%;' : ''" v-loading="isLoading">
    <el-container style="height: 100%;font-size: 14px;color:#999999;">
      <el-aside ref="thumbnailContainer" width="100px" v-if="props.scrollAble"
        style="border-right: 1px solid #000000;text-align: center;background-color: #444444;">
        <div v-for="page in pageSize" :key="page">
          <div @click="clickThumbnailImg(page)" :class="'wdap_view_thumbnail page_' + page">
            <img :ref="(el) => thumImageRefs[page] = (el as Element)" class="wdap_view_imgs_thumbnail" :data-page="page"
              @load="onThumbnailLoad($event, page)"
              :src="Farm2Request.getFullPath('api/exfiles/download/imgs' + page + '/0/' + props.extendFileId)">
            <div style="font-size: 10px;color: #ffffff;"> 第{{ page }}页</div>
          </div>
        </div>
      </el-aside>
      <el-container style="height: 100%;">
        <el-header style="padding: 10px;background-color: #666666;color:#ffffff;"><b>{{ filename }}</b>
          <span style="font-size: 10px;float: right;"> {{ visibleThumbnails }}--{{ visibleImages }}</span></el-header>
        <el-main style="background-color: #666;">
          <div ref="mainContainer" style="padding: 20px;text-align: center;">
            <div v-for="page in pageSize" :key="page">
              <img style="cursor: pointer;" draggable="false"
                @click=" imgViewerRef.show(Farm2Request.getFullPath('api/exfiles/download/imgs' + page + '/3/' + props.extendFileId))"
                :ref="(el) => imageRefs[page] = (el as Element)" :data-page="page" class="wdap_view_imgs_img" lazy
                @load="onImageLoad($event, page)"
                :src="page > 2 ? '/icon/imgSit.png' : (Farm2Request.getFullPath('api/exfiles/download/imgs' + page + '/0/' + props.extendFileId))"
                :data-src="page > 10 ?
                  (Farm2Request.getFullPath('api/exfiles/download/imgs' + page + '/2/' + props.extendFileId))
                  : (Farm2Request.getFullPath('api/exfiles/download/imgs' + page + '/0/' + props.extendFileId))">
              <div style="font-size: 10px;color: #ffffff;"> 第{{ page }}页</div>
            </div>
          </div>
        </el-main>
        <el-footer style="height: 30px;padding: 10px;text-align: center;background-color: #666666;color:#ffffff;">共{{
          pageSize }}页
          {{ farmFileUnitTools.getFileSizeTitle(fileSize) }}
        </el-footer>
      </el-container>
    </el-container>
  </div>
  <view_viewer_image ref="imgViewerRef"></view_viewer_image>
</template>
<script setup lang="ts">
import Farm2Request from '@/service/remoteRequests/Farm2Request';
import type { DataResponse } from '@/types/commons/DataResponse';
import { RequestTypeEnum } from '@/types/commons/RequestTypeEnum';
import notice from '@/components/msg/FarmNotice';
import { nextTick, onMounted, onUnmounted, reactive, ref, watch } from 'vue';
import farmFileUnitTools from '@/hook/farmFileUnitTools';
import view_viewer_image from './view_viewer_image.vue';
//----------------------------------------------------
const imgViewerRef = ref();
const isLoading = ref(false);//加载状态：true|false
const imageRefs = ref<Record<number, Element>>({});
const thumImageRefs = ref<Record<number, Element>>({});
const props = defineProps({
  extendFileId: {
    type: String,
    required: true
  }, scrollAble: {
    type: Boolean,
    required: false,
    default: true,
  }
});
const filename = ref('');
const pageSize = ref(10);
const fileSize = ref(0);

const visibleImages = reactive<number[]>([]); // 存储当前可见的图片序号
const visibleThumbnails = ref<number[]>([]); // 存储当前可见的缩略图序号
// 创建 IntersectionObserver 实例
let observer: IntersectionObserver;
let thumbnailObserver: IntersectionObserver;
const mainContainer = ref();
const thumbnailContainer = ref();
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

/**
 * 监控右侧页面滚动状态
 */
watch(() => {
  return visibleImages[0];
}, (page) => {
  scrollThumToPage(page);
  let nextPage = 0;
  for (const num of visibleImages) {
    if (num > nextPage) {
      nextPage = num;
    }
    const imgElement = imageRefs.value[num];
    (imgElement as unknown as { src: string }).src = imgElement.getAttribute('data-src') || '';
  }
  const nextImgElement = imageRefs.value[nextPage + 1];
  if (nextImgElement) {
    (nextImgElement as unknown as { src: string }).src = nextImgElement.getAttribute('data-src') || '';
  }
});

/**
 * 滚动到指定图片(主图)
 * @param page
 */
const scrollToPage = (page: number) => {
  nextTick(() => {
    const imgElement = imageRefs.value[page];
    if (imgElement) {
      mainContainer.value.scrollTop = 0; // 重置滚动条到顶部（如果需要的话）
      imgElement.scrollIntoView({ behavior: 'smooth' });
    }
  });
};

/**
 * 滚动到指定图片(缩略图)
 * @param page
 */
const scrollThumToPage = (page: number) => {
  nextTick(() => {
    const imgElement = thumImageRefs.value[page];
    if (imgElement) {
      thumbnailContainer.value.scrollTop = 0; // 重置滚动条到顶部（如果需要的话）
      imgElement.scrollIntoView({ behavior: 'smooth' });
    }
    setLiveThumImg(page);
  });
};
/**
 * 给缩略图着色
 */
const setLiveThumImg = (page: number) => {
  const allelements = document.querySelectorAll('.wdap_view_thumbnail');
  allelements.forEach(element => {
    element.setAttribute('style', 'background-color: transparent;');
  });
  const elements = document.querySelectorAll('.wdap_view_thumbnail.page_' + page);
  elements.forEach(element => {
    element.setAttribute('style', 'background-color: #000000;');
  });
}

/**
 * 点击缩略图
 */
const clickThumbnailImg = (page: number) => {
  scrollToPage(page);
  setLiveThumImg(page);
}

/**
 * 获取图片册的总页数，图片册的名称，每个图片的地址
 */
const loadImgsInfo = () => {
  Farm2Request.submit('api/exfiles/download/imgsinfo/' + props.extendFileId, RequestTypeEnum.post, isLoading)
    .then((response: DataResponse) => {
      const pageInfo = response.data as { fileName: string, pageSize: number, size: number };
      filename.value = pageInfo.fileName;
      pageSize.value = pageInfo.pageSize;
      fileSize.value = pageInfo.size;
    }).catch((msg: string) => {
      notice.byError(msg);//报错
    });
}
watch(() => {
  return props.extendFileId;
}, () => {
  loadImgsInfo();
}, { immediate: true });

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//                                      图片滚动定位
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const onImageLoad = (event: Event, page: number) => {
  const imgElement = event.target as HTMLImageElement;
  if (imgElement && observer) {
    observer.observe(imgElement);
  }
};
// 缩略图加载完成后的处理函数
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const onThumbnailLoad = (event: Event, page: number) => {
  const thumbnailElement = event.target as HTMLImageElement;
  if (thumbnailElement && thumbnailObserver) {
    thumbnailObserver.observe(thumbnailElement);
  }
};

onMounted(() => {
  observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        // 如果图片在视口中
        const page = parseInt(entry.target.getAttribute('data-page') || '0');
        if (!visibleImages.includes(page)) {
          visibleImages.push(page);
        }
      } else {
        // 如果图片不在视口中，从数组中移除
        const page = parseInt(entry.target.getAttribute('data-page') || '0');
        const index = visibleImages.indexOf(page);
        if (index > -1) {
          visibleImages.splice(index, 1);
        }
      }
    });
  });
  // 初始化缩略图的 IntersectionObserver
  thumbnailObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        // 如果缩略图在视口中
        const page = parseInt(entry.target.getAttribute('data-page') || '0');
        if (!visibleThumbnails.value.includes(page)) {
          visibleThumbnails.value.push(page);
        }
      } else {
        // 如果缩略图不在视口中，从数组中移除
        const page = parseInt(entry.target.getAttribute('data-page') || '0');
        const index = visibleThumbnails.value.indexOf(page);
        if (index > -1) {
          visibleThumbnails.value.splice(index, 1);
        }
      }
    });
  });

  // 观察已加载的图片元素
  document.querySelectorAll('.wdap_view_imgs_img').forEach(img => {
    observer.observe(img);
  });

  // 观察已加载的缩略图元素
  document.querySelectorAll('.wdap_view_imgs_thumbnail').forEach(thumbnail => {
    thumbnailObserver.observe(thumbnail);
  });
});

onUnmounted(() => {
  // 清理观察者
  observer.disconnect();
  thumbnailObserver.disconnect();
});
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
</script>
<style scoped>
/* 缩略图 */
.wdap_view_imgs_thumbnail {
  max-width: 100%;
  pointer-events: none;
  user-select: none;
  -webkit-user-select: none;
}

/* 缩略图外框 */
.wdap_view_thumbnail {
  padding: 4px;
  border-radius: 4px;
  margin: 4px;
  cursor: pointer;
}

/* 主图 */
.wdap_view_imgs_img {
  width: 100%;
  max-width: 800px;
  margin-top: 8px;
  user-select: none;
  -webkit-user-select: none;
}
</style>
