# <img src="https://github.com/Cobitsa/App/blob/develop/logo.png?raw=true" alt="logo" width="100%"/>

## 개요

본 프로젝트는 [청와대 국민청원](https://www1.president.go.kr/petitions/583770) 에서 135명의 시각장애인을 대상으로 진행한 설문조사 중 전맹 시각장애인의 82% 가 가장
이용하기 어려운 교통수단으로 버스를 꼽은것을 보고 시작한 프로젝트이다. 시각장애인이 이용상의 불편함으로 인하여 이동 시에 대중교통을 선택하지 않는 상황이 발생하지 않도록,
대중교통의 이용경험에서 제시된 불편한 점을 개선할 수 있도록 함이 목적입니다.

<iframe width="1280" height="720" src="https://www.youtube.com/embed/ze7qtyzc1TM" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

## 프로젝트 목표

- 정류장 위치 식별의 어려움을 개선  
  버스 정류장을 찾기 어려우며 올바른 위치에 있는지 확인하기 어렵다고 한다. 버스 정류장을 찾았다고 할지라도 해당 버스 정류장에 대한 정보(이름, 정류장 고유번호 등)를 알 수 없어 혼란을 겪게 된다.
- 버스 번호를 파악할 수 없는 어려움을 개선  
  가장 많은 시각장애인이 불편함을 호소한 문제점이다. 정류장에서 '00번 버스가 곧 도착합니다.'라는 안내음이 송출된다고 할지라도, 버스가 안내된 순서대로 진입하지 않기 때문에 어느 버스에 탑승해야 하는지에 대한
  정보를 얻을 수 없는 것은 여전하다.
- 하차 벨 위치를 파악할 수 없어서 생기는 어려움을 개선  
  내려야 하는 정류장에서 하차하기 위해서는 하차 벨을 눌러야 한다. 그러나 하차 벨 위치를 알 수 없어 내려야 했던 정류장보다 한 정류장을 지나쳐 내린 경험을 한 시각장애인이 적지 않다.

## 구현 기능
1. 정류장 식별 보조 기능  
   현재 GPS 위치 기반으로 정류장 정보 제공
2. 버스 식별 보조 기능
    1. 탑승할 버스를 지정하여 해당 버스의 도착 알림 제공
    2. 버스 입구에서 음성으로 버스 번호 알림 제공
3. 모바일 하차 벨 시스템  
   하차할 정류장을 지정하여 이전 정류장 출발시 하차벨 작동 및 알림 제공
4. 버스 기사 알림 기능  
   버스에 탑승/하차 요청 발생시 버스기사 단말에서 알림 제공

## 시스템 개념도

<img src="https://github.com/Cobitsa/App/blob/develop/diagram.png?raw=true" alt="개념도" width="100%"/>

## 구현 결과
본 시스템의 주 이용자는 전맹 시각장애인이기 때문에 음성인식 기반으로 작동이 되며, 약맹 시각장애인들도 이용하기 좋게 시각적인 요소도 고려하였다.
모든 기능은 제일 하단의 음성인식버튼을 눌러 음성인식 기능을 호출한 후 이루어 지며, 모든 화면요소는 접근성 옵션의 도움을 받아 문제없이 작동할 수 있도록 라벨링 처리를 하였다.
1. 정류장 식별 보조 기능  
   여기 어디야 명령을 할 시 현재 위치한 정류장의 이름과 정류장 번호를 음성으로 알림과 동시에 화면에 출력한다.
2. 버스 식별 보조 기능
   00 번 버스 탈래 라고 명령을 할 때 해당 정류장에 도착하는 버스의 정보와 비교하여 가장 빨리 오는 버스의 승차를 예약한다.
   이 때 정류장에 오지 않는 버스를 예약하였을 시 노선정보가 없는 버스임을 알린다. 버스 승차 예약이 완료되면 화면에 버스번호와 버스의 노선색이 출력되며
   버스가 이전 정류장을 출발하였을 시 진동과 음성으로 출발하였음을 알리고, 버스는 정류장에 진입할 때 시각장애인 탑승여부를 전달받으며 버스번호가 입구에서 울리게 된다.
3. 모바일 하차 벨 시스템  
   00 정류장에서 내릴게 라고 명령을 할 시 버스가 도달하게 될 정류장 리스트와 비교하여 하차 예약된 정류장의 이전정류장을 출발 한 후 사용자에게
   진동과 음성으로 알림을 주며, 버스 단말과 연동되어 하차벨이 작동되며 시각장애인이 하차할것이라는 알림을 추가적으로 준다.
4. 버스 기사 알림 기능  
   버스 다말기에 시각장애인의 탑승/하차 여부를 알려준다.

<figure style="width:30%;float:left;margin: 1.6%;">
<img src="https://github.com/Cobitsa/App/blob/develop/findStation.png?raw=true" alt="정류장 조회" width="100%"/>
<figcaption style="text-align: center">1. 정류장 조회</figcaption>
</figure>
<figure style="width:30%;float:left;margin: 1.6%;">
<img src="https://github.com/Cobitsa/App/blob/develop/reserveBus.png?raw=true" alt="승차 예약" width="100%"/>
<figcaption style="text-align: center">2. 승차 예약</figcaption>
</figure>
<figure style="width:30%;float:left;margin: 1.6%;">
<img src="https://github.com/Cobitsa/App/blob/develop/reserveStation.png?raw=true" alt="하차 정류장 예약" width="100%"/>
<figcaption style="text-align: center">3. 하차 정류장 예약</figcaption>
</figure>
<br/>
<figure>
<img src="https://github.com/Cobitsa/App/blob/develop/bus.png?raw=true" alt="버스 앱" width="100%"/>
<figcaption style="text-align: center">4. 버스 기사 알림</figcaption>
</figure>