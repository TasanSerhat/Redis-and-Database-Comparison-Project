# Redis vs Database Performans Analizi (Spring Boot)

Bu proje, geleneksel disk tabanlı veritabanı sorguları (Disk I/O) ile bellek içi (In-Memory) Redis Cache kullanımının performans farkını canlı ve interaktif bir şekilde karşılaştırmak amacıyla hazırlanmıştır. Teknik sunumlar veya eğitim amaçlı kullanıma oldukça uygundur.

## Proje Hakkında
Uygulama, sistem ayağa kalkarken H2 fiziksel veritabanına (dosya tabanlı) 50.000 adet test kullanıcısı (Big Data) kaydeder. Ardından, index'i olmayan (Full Table Scan gerektiren) ağır bir e-posta arama sorgusu yapılarak, veritabanının gerçek senaryolarda nasıl yavaşladığı simüle edilir. 

Aynı ağır sorgu Spring Boot'un `@Cacheable` anotasyonu kullanılarak Redis üzerinden de geçirilir. Uygulamanın içerisine gömülü olan şık ve animasyonlu Web Dashboard'u sayesinde, veritabanının yavaşlığına karşı Redis'in hızı adeta bir "yarış" şeklinde görselleştirilir.

## Kullanılan Teknolojiler
* **Java 17 & Spring Boot 3.2.x**
* **Spring Data JPA & Hibernate**
* **Spring Data Redis**
* **H2 Database** (File-Based modunda disk simülasyonu için)
* **Lombok**
* **Vanilla HTML / CSS (Glassmorphism) / JavaScript** (Frontend)

## Öne Çıkan Özellikler
* **Canlı Dashboard:** Herhangi bir JS framework'ü kurmaya gerek kalmadan doğrudan Spring Boot üzerinden hizmet veren animasyonlu, koyu temalı (dark mode) performans ekranı.
* **Gerçek Veri Akışı (Log Konsolu):** Dashboard'un altındaki terminal görünümlü konsolda, sadece sürelerin değil; o an DB'den veya Redis'ten çekilen gerçek verilerin (ID, İsim vb.) canlı akışı.
* **İkna Edici Kanıtlar:** `spring.jpa.show-sql=true` ayarı sayesinde, DB testi yapıldığında IDE konsolunda akan yüzlerce satırlık SQL sorgusunun aksine, Redis testinde konsolun tamamen sessiz kalmasının gösterimi.

## Kurulum ve Çalıştırma

### Gereksinimler
* Java 17 veya üzeri
* Maven
* Bilgisayarınızda kurulu ve çalışan bir **Redis Sunucusu** (Varsayılan ayarlar: `localhost:6379`)

### Çalıştırma Adımları
1. Projeyi bilgisayarınıza klonlayın:
   ```bash
   git clone https://github.com/TasanSerhat/Redis-Sample-Project.git
   ```
2. Maven komutuyla uygulamayı başlatın:
   ```bash
   mvn spring-boot:run
   ```
3. *(Not: İlk açılışta 50.000 verinin fiziksel diske yazılması bilgisayar hızınıza göre birkaç saniye sürebilir.)* "50.000 test verisi eklendi" yazısını gördükten sonra tarayıcınızı açın:
   ```text
   http://localhost:8081
   ```
4. Arayüzdeki **"Canlı Analizi Başlat"** butonuna basın ve farkı gözlemleyin.

---
*Bu proje Redis önbellekleme mimarisinin gücünü somut bir şekilde göstermek için hazırlanmıştır.*
