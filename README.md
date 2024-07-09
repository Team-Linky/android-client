## 링키가 무엇인가요?

링키는
1. 유튜브, 인스타그램, 페이스북 등 다양한 자료를 수집하여 관리하는 디지털 콘텐츠 큐레이션 서비스입니다.
2. 웹 사이트의 URL을 저장하여 OpenGraph 기반으로 타임라인에서 관리할 수 있습니다.
3. 생체 인식, Pin 암호 기능을 지원하여 물리적 데이터를 보호합니다.
4. 테마 시스템 설정에 따라 다크, 라이트 모드를 지원하여 눈의 피로도를 최소화합니다.

## 링키는 이런 과정으로 탄생했어요.

|담당|이름|설명|
|---|---|---|
|기획|강성범|링키라는 서비스를 기획하여 팀을 구축했습니다.|
|디자인|김예진|링키만의 UI/UX를 디자인하고 피그마를 활용하여 디자인 시스템을 구축했습니다.|
|iOS|추연학|링키의 iOS 앱을 설계, 개발, 배포 및 유지보수까지 iOS의 모든것을 담당합니다.|
|Android|김민석|링키의 Android 앱을 설계, 개발, 배포 및 유지보수까지 Android의 모든 것을 담당합니다.|

## 링키 안드로이드는 이러한 기술들이 사용중이에요.
- Kotlin 2.0
- UI 작업: Jetpack Compose, ViewModel, Lifecycle, Paging, Coil, Material, Balloon
- 아키텍처: Orbit-MVI, Clean Architecture, Multi Module
- 의존성 주입: Hilt
- 직렬화/역직렬화: Moshi, Kotlinx Serialization
- 생체 인식: Jetpack Biometric
- DataBase
    - 타임라인, 태그의 데이터는 Jetpack Room에서
    - 화면 잠금 여부, 생체 인식 여부, PIN 번호는 Preferences-DataStore에서 관리하고있어요.

# 미리보기

https://github.com/Team-Linky/android-client/assets/13445643/cd13fdb5-6122-4d8a-997b-f20327b25faf

ⓒ 2024 Linky - (강성범, 김예진, 추연학, 김민석)
