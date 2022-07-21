# LunchBox_release

*상업적 목적으로 사용을 금지합니다.

*You are prohibited from using it for commercial purposes.

<br/>

### ~~이 Project는 Refactoring 중 입니다.~~

### Refactoring이 완료 되었습니다.(2022-04-22)



<br/>

플레이 스토어에서 다운받으실 수 있습니다.

다운로드 링크: https://play.google.com/store/apps/details?id=com.kimleehanjang.lunchbox

<br/>

<br/>

## Specification

| Tech Stack           | Contents                                                     |
| -------------------- | ------------------------------------------------------------ |
| Architecture         | MVVM                                                         |
| Jetpack              | ViewBinding, LiveData, ViewModel, lifeCycle                  |
| Dependency Injection | dagger-Hilt                                                  |
| Design Pattern       | Repository Pattern                                           |
| Async                | Coroutine                                                    |

<br/>

<br/>

## Wiki

구현 설명이 적혀 있는 wiki

[https://github.com/Moony-H/LunchBox_release/wiki](https://github.com/Moony-H/LunchBox_release/wiki)
<br/>

<br/>

# 앱 소개

GPS를 활용하여 주위에 있는 음식점을 탐색할 수 있는 앱 입니다.

<br/>

<br/>

## 1. 튜토리얼

최초 실행 시, 튜토리얼이 실행된다.

<br/>

![1 최초 실행 튜토리얼](https://user-images.githubusercontent.com/53536205/145452554-4f1667d3-0df7-4767-8847-dd0db4cb66c3.gif)



<br/>

<br/>



## 2. GPS로 현재 위치 파악, 지정

GPS로 현재 위치를 알 수 있고, 그 위치로 지정한다.

![2 gps로 현재 위치 파악](https://user-images.githubusercontent.com/53536205/145452634-a5c91585-cbed-4004-b212-b9ff3b121bea.gif)



<br/>

<br/>

## 3. 지도를 클릭하여 위치 선정

지도를 클릭하여 원하는 위치로 변경

![3 또는 지도를 클릭하여 위치 선정](https://user-images.githubusercontent.com/53536205/145452669-a5f8057b-9e34-4b62-9cf5-ee2914724541.gif)



<br/>

<br/>

## 4. 장소를 검색하여 위치 선정

또는 장소를 직접 검색하여 위치를 선정할 수 있다.

![4 또는 검색하여 위치 지정](https://user-images.githubusercontent.com/53536205/145452719-f74a535f-5b21-414c-9922-544bab3dcdb9.gif)



<br/>

<br/>


## 5. 드래그하여 반경을 지정

마커를 드래그하여 원을 그려 탐색할 반경을 원하는 만큼 설정할 수 있다.

![5 드래그하여 반경 지정](https://user-images.githubusercontent.com/53536205/145452766-431fcacc-2c1a-47e9-9bbf-1b240bf634f0.gif)



<br/>

<br/>



## 6. 카테고리로 원하는 음식 탐색

자신이 원하는 음식 종류로 음식점을 탐색할 수 있다.

![6 카테고리로 원하는 음식 탐색](https://user-images.githubusercontent.com/53536205/145452800-afed9b1c-e25f-4fe2-af2a-9bc04b8c0da8.gif)



<br/>

<br/>

## 7. 리스트로 음식점 모아보기

리스트로 맵에 있는 음식점들을 한눈에 모아 볼 수 있고, 리스트를 클릭하면 상세 페이지를 볼 수 있다.

![7 리스트로 한눈에 모아 보기](https://user-images.githubusercontent.com/53536205/145452882-f82c1023-7973-4eec-8119-989ffc346a8d.gif)



<br/>

<br/>

## 8. 아이콘을 클릭하여 상세 보기

또는 맵 위에 있는 마커를 클릭하여 음식점의 상세 페이지를 볼 수 있다.

![8 또는 아이콘을 클릭하여 상세 보기](https://user-images.githubusercontent.com/53536205/145452929-db3ac8ae-ed36-456c-96fe-c370f8137f06.gif)



<br/>

<br/>

## 9. 랜덤 선택

랜덤 선택 버튼을 클릭하여, 내가 고른 카테고리 안에서 랜덤하게 음식점을 선택할 수 있다.

![9 고르기 어려울땐 랜덤 선택](https://user-images.githubusercontent.com/53536205/145452970-d74f7768-8321-4ba3-bbfc-797833c9fdfd.gif)



<br/>

<br/>

## 10. 카카오 맵으로 연결하여 길 찾기

카카오 맵으로 음식점까지의 길을 찾을 수 있다.

![10 카카오 맵으로 연동하여 길 찾기](https://user-images.githubusercontent.com/53536205/145453008-bede38f2-c68b-4a46-b214-4ce9cedd2551.gif)



<br/>

<br/>

## 직접 만든 뷰 설명

<br/>

지도 밑을 일부 가리고, 드래그 하여 올렸다 내렸다 할 수 있는 뷰를 기획했습니다.

<br/>

하지만 지도 Fragment 가 TouchEvent를 전부 가져가는 일이 발생하였고, 따라서 지도 Fragment의 부모 Layout에 터치 이벤트 Intercept 를 할 수 있는 ViewGroup을 직접 만들었습니다.





<br/>

<br/>

## DrawerLayout(moony.SlideLayout)



뷰를 슬라이드 하여 화면을 일부, 또는 전체를 가릴 수 있게 하는 ViewGroup을 만들었습니다.

이 뷰 그룹은 단 두개의 자식 View만을 가질 수 있으며, 모두 match_parent 속성이어야 합니다.

<br/>

**첫번째 자식 뷰는 배경을 담당하는 MainView, 두 번째 자식 뷰는 슬라이드가 되는 SlideView 입니다.**

<br/>

안드로이드에 있는 ViewGroup을 상속하여 만들었습니다.

또한 ViewDragHelper라는 클래스를 이용하였습니다.

<br/>

## 기능 1) 슬라이드



![UpDown](https://user-images.githubusercontent.com/53536205/158807595-076c402c-462a-463d-8e35-50e1350d7ee1.gif)





뷰를 잡아 끌어 뷰를 이동시킬 수 있습니다.

<br/>

SlideLayout이 사용자의 터치 속도를 계산하여 일정 범위를 넘어 서면 onInterceptTouchEvent를 발동시켜, 다음 자식 뷰 까지 터치가 넘어가지 않고 중간에 가로챕니다.

<br/>

가로챈 터치는 **SlideView**를 움직이기 위해 사용됩니다. 이 때 활용한 것이 ViewDragHelper 입니다.

<br/>

```kotlin
override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //단 한번이라도 return true를 하면 여기서는 MOVE,Up 을 처리하지 않는다.
        when(ev.actionMasked){
            MotionEvent.ACTION_DOWN->{
                maxVelocity=0F
                //속도 트랙커 초기화
                mVelocityTracker?.clear()
                //속도 트랙커가 null 이면 생성.
                mVelocityTracker=mVelocityTracker?: VelocityTracker.obtain()

                //... 중략
                //.
                //.
                //.

            }
            MotionEvent.ACTION_MOVE->{
                //... 중략
                //.
                //.
			

                //

                    if(dy>=touchSlop){

                        isSliding=true

                        //캡쳐 할 수 있게 강제 ACTION_DOWN event 설정.
                        val forcedEvent=MotionEvent.obtain(ev)
                        forcedEvent.action=MotionEvent.ACTION_DOWN
                        mViewDragHelper.processTouchEvent(forcedEvent)
                        return true
                    } else{
                        isSliding=false
                        return false
                    }
                }else{
                    //슬라이딩 중이 아니면(어차피 isSliding 은 false)
                    return isSliding
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{



                isSliding=false
                isSlideViewTouched=false
                isScrollViewTouched=false
                //mViewDragHelper.shouldInterceptTouchEvent(ev)
                return mViewDragHelper.shouldInterceptTouchEvent(ev)




            }


            else -> {
                return mViewDragHelper.shouldInterceptTouchEvent(ev)
            }
        }

    }
```

<br/>

xml 에서 app:moony_SlideLayout_exposureHeight 의 옵션을 사용하여 기본적으로 노출 되어있는 SlideView의 높이를 지정할 수 있습니다.

<br/>

또한 app:moony_SlideLayout_maxHeight의 옵션을 사용하여, 잡아 끌어 당겨서 최대로 올릴 수 있는 높이 또한 지정할 수 있습니다.

<br/>

<br/>

(200dp 일 때)

```xml
<moony.SlideLayout
    android:id="@+id/test_slide_layout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:moony_SlideLayout_exposureHeight="200dp"
    app:moony_SlideLayout_maxHeight="600dp"
    >
```

![화면 캡처 2022-03-17 215312](https://user-images.githubusercontent.com/53536205/158812808-3f0a2e74-db0e-43d6-a966-7c268ed4b015.png)



<br/>

<br/>

(500dp 일 때)

```xml
<moony.SlideLayout
    android:id="@+id/test_slide_layout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:moony_SlideLayout_exposureHeight="500dp"
    app:moony_SlideLayout_maxHeight="800dp"
    >
```

![화면 캡처 2022-03-17 215348](https://user-images.githubusercontent.com/53536205/158813127-99cb858d-fbbb-4f20-9965-c845d7359463.png)





<br/>

<br/>

## 기능2) 애니메이션

<br/>

사용자가 지정한 크기의 절반 만큼을 Threshold로 설정하여, 접히는지 아닌 지를 판정할 수 있습니다.

<br/>

판정 후에는 ViewDragHelper의 Animation을 사용하여 자동으로 지점까지 움직입니다.

<br/>

![Animation](https://user-images.githubusercontent.com/53536205/158811021-0db1c31a-6e1b-49bd-8ada-918b72db7a40.gif)



<br/>

<br/>

또한 사용자가 뷰를 잡아 끌기 시작하는 때, 혹은 접히거나 펼쳐지는 애니메이션이 끝난 후,

이 두 시점에 **람다**를 이용하여 Callback을 받을 수 있습니다.

```kotlin
binding.testSlideLayout.setOnSlideViewCaptured {  }
binding.testSlideLayout.setOnSlideViewReleased {  }
```



<br/>

<br/>

## 기능3) 스크롤 뷰 등록

<br/>

스크롤 뷰를 등록하면, 등록된 뷰를 터치하는 동안에는 **slideLayout이 움직이는데 영향을 미치지 않습니다.** 

<br/>

하지만, ScrollView가 끝까지(맨 처음의 목록까지) 올라갔을 경우, **SrollView를 슬라이드 하면 slideLayout이 움직입니다.**

<br/>

![Scroll](https://user-images.githubusercontent.com/53536205/158814764-228377b9-905e-4057-a32a-0ca925197be1.gif)

<br/>

<br/>

scrollView는 app:moony_SlideLayout_scrollViewId 옵션을 이용하여 등록할 수 있습니다.

```xml
<moony.SlideLayout
    android:id="@+id/test_slide_layout"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:moony_SlideLayout_exposureHeight="500dp"
    app:moony_SlideLayout_maxHeight="800dp"
    app:moony_SlideLayout_scrollViewId="@id/test_list_view"
    >
```

<br/>

<br/>

만약 생명주기에 맞춰야 할 경우, 객체의 메소드를 이용해서도 scrollView를 등록할 수 있습니다.

```kotlin
binding.testSlideLayout.setScrollView(R.id.test_list_view)
```



<br/>

**전체 소스코드는 밑의 링크에서 확인할 수 있습니다.**

https://github.com/Moony-H/LunchBox_release/blob/master/app/src/main/java/moony/SlideLayout.kt

