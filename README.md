# MINPRO 3 PBO

| | |
|---|---|
| **Nama** | Muhammad Aidil Fadilah |
| **NIM** | 2509116032 |
| **Kelas** | Sistem Informasi A 2025 |

---

## Daftar Isi

1. [Deskripsi Singkat Program](#1-deskripsi-singkat-program)
2. [Penjelasan Struktur Package](#2-penjelasan-struktur-package)
3. [Penjelasan Alur Program](#3-penjelasan-alur-program)
4. [Penerapan Encapsulation dan Inheritance](#4-penerapan-encapsulation-dan-inheritance)
5. [Penerapan Polymorphism dan Abstraction](#5-penerapan-polymorphism-dan-abstraction)
6. [Penerapan Nilai Tambah (Interface)](#6-penerapan-nilai-tambah-interface)
7. [Dokumentasi Tampilan Program](#7-dokumentasi-tampilan-program)

---

## 1. Deskripsi Singkat Program

Sistem Manajemen Donasi Darah adalah program CRUD (Create, Read, Update, Delete) berbasis Java yang berjalan lewat command line. Program mengelola dua jenis data, yaitu **data pendonor** dan **data riwayat donasi darah**.

Pada Minpro 3 ini, program dari Minpro 2 dikembangkan dengan:

- Struktur proyek MVC (Model, View, Controller) yang dipisahkan ke dalam package masing-masing.
- Polymorphism (overriding dan overloading).
- Abstraction (abstract class dan abstract method).
- Interface sebagai nilai tambah.
- Keyword final pada atribut, method, dan class yang tidak boleh berubah.
- Error handling menggunakan exception, sehingga program tidak langsung berhenti ketika terjadi kesalahan input.
- Subclass DonorBaru dan DonorTetap sekarang punya atribut pembeda.
- Inisialisasi ArrayList yang redundan sudah dirapikan.

---

## 2. Penjelasan Struktur Package

Seluruh kode berada di bawah package com.mycompany.miniproject dan dibagi menjadi tiga bagian sesuai pola MVC.

```
com.mycompany.miniproject
├── MiniProject.java                    (entry point)
│
├── model                               (M - Model)
│   ├── DapatDitampilkan.java           (interface)
│   ├── Donor.java                      (abstract class)
│   ├── DonorBaru.java
│   ├── DonorTetap.java
│   ├── RiwayatDonasi.java
│   ├── DonasiDarahManager.java
│   └── DataTidakDitemukanException.java
│
├── view                                (V - View)
│   └── ConsoleView.java
│
└── controller                          (C - Controller)
    └── DonasiDarahController.java
```

### Peran setiap bagian

| Package | Class | Peran |
|---|---|---|
| (root) | MiniProject | Entry point. Membuat objek Model, View, dan Controller, lalu menjalankan program. Bukan bagian M, V, maupun C. |
| model | Donor | Abstract class berisi data dan validasi yang sama untuk semua donor. |
| model | DonorBaru | Subclass donor baru, dengan atribut khusus sumberInformasi. |
| model | DonorTetap | Subclass donor tetap, dengan atribut khusus nomorKartu dan jumlahDonasi. |
| model | RiwayatDonasi | Data satu kali donasi (tanggal dan jumlah kantong darah). |
| model | DonasiDarahManager | Menyimpan list donor dan donasi, serta logika tambah, cari, update, dan hapus. |
| model | DapatDitampilkan | Interface yang menjadi kontrak agar objek bisa ditampilkan oleh View. |
| model | DataTidakDitemukanException | Exception khusus ketika ID donor/donasi tidak ditemukan. |
| view | ConsoleView | Satu-satunya class yang membaca input dan mencetak output di konsol. |
| controller | DonasiDarahController | Mengatur alur menu: mengambil input dari View, memanggil Model, menangkap exception, lalu meminta View menampilkan hasil. |

### Aturan hubungan antar bagian

- Model tidak mengenal View maupun Controller, dan tidak memakai Scanner atau System.out.
- View hanya membaca dan menampilkan, tanpa aturan bisnis.
- Controller adalah satu-satunya yang mengenal Model dan View.

---

## 3. Penjelasan Alur Program

### Alur umum data

```
Pengguna -> View (baca input) -> Controller -> Model (proses & validasi)
                                      |
Pengguna <- View (tampilkan)  <- Controller <- hasil / exception
```

### Langkah-langkah

1. Program dimulai dari MiniProject.main(), yang membuat DonasiDarahManager (Model), ConsoleView (View), dan DonasiDarahController (Controller), lalu memanggil controller.jalankan().
2. Saat Model dibuat, program mengisi data awal berupa 2 donor (Siti Aminah sebagai Donor Baru dan Budi Santoso sebagai Donor Tetap) dan 1 riwayat donasi, sehingga menu Lihat Data langsung menampilkan isi.
3. Controller menampilkan Menu Utama di dalam perulangan while, dan program terus berjalan sampai pengguna memilih 0 (Keluar).
4. Dari Menu Utama, pengguna masuk ke salah satu submenu lewat switch:
   - Kelola Data Donor: Tambah, Lihat Semua, Update, Hapus, dan Cari Donor (berdasarkan nama).
   - Kelola Data Donasi: Catat, Lihat Semua, Update, dan Hapus Donasi.
   - Lihat Ringkasan: total donor, total donasi, dan total kantong darah.
5. Pada Tambah Donor, pengguna memilih kategori (Donor Baru atau Donor Tetap). Input tambahan menyesuaikan kategori: Donor Baru ditanya sumber informasi, Donor Tetap ditanya jumlah donasi sebelumnya.
6. Pada Catat Donasi, donasi dikaitkan ke ID donor yang sudah ada. Untuk Donor Tetap, jumlah donasinya otomatis bertambah satu (lewat method catatDonasi() yang di-override).
7. Pada Hapus Donor dan Hapus Donasi, program meminta konfirmasi y/n. Menghapus donor juga menghapus riwayat donasi miliknya.
8. Data disimpan sementara di ArrayList, sehingga hilang saat program ditutup.

### Error handling

- Input angka dibaca oleh ConsoleView.bacaAngka(), yang mengulang pertanyaan sampai pengguna memasukkan angka yang valid, jadi huruf atau input kosong tidak membuat program crash.
- Validasi data (nama kosong, golongan darah selain A/B/AB/O, umur di luar 17-65, no. telepon tidak valid, tanggal salah format atau di masa depan, jumlah kantong kurang dari 1) dilakukan di Model dengan melempar IllegalArgumentException.
- ID yang tidak ada akan melempar DataTidakDitemukanException.
- Controller menangkap kedua exception tersebut dan meminta View menampilkan pesan [GAGAL] ..., lalu pengguna kembali ke menu.
- Update data bersifat **atomik**: semua input divalidasi dulu, sehingga bila ada satu yang salah tidak ada data yang berubah sebagian.

---

## 4. Penerapan Encapsulation dan Inheritance

### Encapsulation

- Semua atribut pada Donor, DonorBaru, DonorTetap, dan RiwayatDonasi bersifat private, sehingga tidak bisa diubah langsung dari luar class.
- Setiap atribut memiliki getter public untuk membaca nilai, dan setter public untuk mengubah nilai.
- Setiap setter memvalidasi nilai sebelum disimpan. Jika tidak valid, setter melempar IllegalArgumentException dan nilai lama tetap dipertahankan.
- Constructor memanggil setter (bukan this.atribut = nilai), sehingga validasi tetap berlaku saat objek pertama kali dibuat.
- Update data lewat perbaruiData(...)` pada objek yang sudah tersimpan di list, sehingga encapsulation dipakai dalam alur program.
- Konstanta aturan bisnis disimpan sebagai public static final: UMUR_MINIMAL, UMUR_MAKSIMAL, dan GOLONGAN_VALID.

#### Penerapan keyword `final`

| Letak | Alasan |
|---|---|
| Atribut id di Donor | ID tidak boleh berubah setelah objek dibuat. |
| Atribut idDonasi dan idDonor di RiwayatDonasi | Identitas donasi dan pemiliknya tidak boleh berubah. |
| Atribut nomorKartu di DonorTetap | Nomor kartu dibuat otomatis dari ID dan tidak boleh diganti. |
| Konstanta UMUR_MINIMAL, UMUR_MAKSIMAL, GOLONGAN_VALID | Aturan bisnis yang bersifat tetap. |
| Setter di Donor (setNama, setUmur, dan lainnya) | Dipanggil di constructor, jadi tidak boleh di-override oleh subclass. |
| Class DonorBaru, DonorTetap, RiwayatDonasi | Tidak dirancang untuk diwariskan lagi. |
| Atribut list di DonasiDarahManager, serta manager dan view di Controller | Referensi objek tidak boleh diganti setelah diinisialisasi. |

### Inheritance

- Donor adalah superclass abstract yang menyimpan atribut dan method yang dimiliki semua donor: id, nama, golonganDarah, umur, noTelepon, beserta getter, setter, dan formatData().
- Dua subclass mewarisi Donor, masing-masing dengan atribut pembeda:

| Subclass | Atribut khusus | Keterangan |
|---|---|---|
| DonorBaru | sumberInformasi | Dari mana donor tahu program donor darah. |
| DonorTetap | nomorKartu, jumlahDonasi | Nomor kartu otomatis (misalnya KD-0002) dan jumlah donasi yang sudah dilakukan. |

- Kedua subclass memanggil constructor superclass lewat `super(...)` untuk mengisi atribut bersama, lalu mengisi atribut khususnya sendiri.
- List<Donor> di DonasiDarahManager dapat menyimpan objek DonorBaru maupun DonorTetap sekaligus, karena keduanya adalah Donor.

---

## 5. Penerapan Polymorphism dan Abstraction

### Abstraction

Donor adalah abstract class dengan dua abstract method yang sengaja tidak diberi isi, karena setiap subclass wajib mengisinya sendiri:

```java
public abstract String getKategori();
public abstract String getInfoKhusus();
```

Dengan begitu Donor tidak bisa dibuat objeknya secara langsung. Objek yang dibuat hanya DonorBaru atau DonorTetap.

### Polymorphism: Overriding

| Method | Letak override | Perilaku |
|---|---|---|
| getKategori() | DonorBaru, DonorTetap | Mengembalikan "Donor Baru" atau "Donor Tetap". |
| getInfoKhusus() | DonorBaru, DonorTetap | Donor Baru menampilkan sumber informasi; Donor Tetap menampilkan nomor kartu dan jumlah donasi. |
| catatDonasi() | DonorTetap | Di Donor isinya kosong; di DonorTetap menambah jumlahDonasi` sebanyak satu. |
| formatRingkas() | Donor, RiwayatDonasi | Menimpa method default milik interface dengan format ringkas masing-masing. |
| toString() | Donor, RiwayatDonasi | Mengembalikan teks ringkas objek. |

Method formatData() di Donor memanggil getKategori() dan getInfoKhusus(). Karena kedua method itu polymorphic, baris Kategori dan info khusus yang tampil otomatis berbeda tergantung objeknya DonorBaru atau DonorTetap, tanpa perlu menulis ulang formatData() di tiap subclass.

Pemanggilan donor.catatDonasi() di DonasiDarahManager.tambahDonasi() juga polymorphic: objek DonorTetap menambah hitungan donasinya, sedangkan DonorBaru tidak berubah, tanpa if atau instanceof.

### Polymorphism: Overloading

| Method | Variasi parameter |
|---|---|
| Constructor DonorBaru | Dengan dan tanpa sumberInformasi |
| Constructor RiwayatDonasi | Tanggal sebagai LocalDate, tanggal sebagai String, dan tanpa jumlah kantong (default 1) |
| setTanggalDonasi di RiwayatDonasi | Parameter LocalDate dan String |
| tambahDonor di DonasiDarahManager | Parameter terakhir String (sumber informasi, menghasilkan DonorBaru) dan int (jumlah donasi, menghasilkan DonorTetap) |
| cariDonor di DonasiDarahManager | Berdasarkan int (ID) dan String (potongan nama) |
| tambahDonasi di DonasiDarahManager | Dengan dan tanpa jumlahKantong |
| formatData di Donor | Tanpa parameter dan dengan boolean ringkas |
| bacaAngka di ConsoleView | Tanpa batas dan dengan batas min dan max |
| tampilkanData di ConsoleView | Menerima satu objek dan menerima list objek |

---

## 6. Penerapan Nilai Tambah (Interface)

Nilai tambah yang saya terapkan adalah interface DapatDitampilkan di package model.

```java
public interface DapatDitampilkan {
    String formatData();
    default String formatRingkas() {
        return formatData();
    }
}
```

### Letak penerapan

| Letak | Peran |
|---|---|
| Donor implements DapatDitampilkan | Donor dapat ditampilkan oleh View. |
| RiwayatDonasi implements DapatDitampilkan | Riwayat donasi juga dapat ditampilkan oleh View. |
| ConsoleView.tampilkanData(DapatDitampilkan data) | Menampilkan satu objek apa pun yang mengimplementasikan interface. |
| ConsoleView.tampilkanData(List<? extends DapatDitampilkan>, String) | Menampilkan banyak objek sekaligus. |

### Manfaat

- Donor dan RiwayatDonasi tidak berhubungan lewat pewarisan, tetapi bisa ditampilkan lewat satu method yang sama karena berbagi kontrak yang sama.
- **Model tidak mencetak apa pun. Model hanya mengembalikan teks lewat formatData(), dan View yang mencetaknya, sehingga pemisahan MVC tetap terjaga.
- View tidak bergantung pada class tertentu. Class baru cukup implements DapatDitampilkan agar langsung bisa ditampilkan tanpa mengubah kode View.
- Method formatRingkas() bersifat default, sehingga class yang tidak membutuhkannya tidak wajib mengimplementasikannya.

---

## 7. Dokumentasi Tampilan Program

### Tampilan Awal

<img width="431" height="208" alt="image" src="https://github.com/user-attachments/assets/d1d33874-a7bc-4b54-8c93-06bfb6cc08b4" />

**Gambar 1.** Menu utama saat program pertama kali dijalankan, berisi Kelola Data Donor, Kelola Data Donasi, Lihat Ringkasan, dan Keluar.

---

### 7.1 Kelola Data Donor

<img width="328" height="253" alt="image" src="https://github.com/user-attachments/assets/464870f9-1954-4222-aa11-0172b97afb56" />

**Gambar 2.** Submenu Kelola Data Donor dengan lima pilihan: Tambah, Lihat Semua, Update, Hapus, dan Cari Donor (nama), serta opsi kembali ke Menu Utama.

<img width="670" height="279" alt="image" src="https://github.com/user-attachments/assets/a86f78d8-c128-45c0-ba8f-c8cadb743b54" />

**Gambar 3.** Menambah **Donor Baru** bernama Fadlan. Selain data umum, program menanyakan sumber informasi, lalu membuat objek DonorBaru dengan ID 3.

<img width="421" height="285" alt="image" src="https://github.com/user-attachments/assets/5bac9e5e-48f2-472f-acc4-5bcb84c882f9" />

**Gambar 4.** Menambah **Donor Tetap** bernama Aidil. Program menanyakan jumlah donasi sebelumnya (3), lalu membuat objek DonorTetap dengan ID 4.

<img width="331" height="1059" alt="image" src="https://github.com/user-attachments/assets/3ab2c882-d2cb-4454-98c3-1ad9a52583f0" />

**Gambar 5.** Menampilkan seluruh donor (4 data: 2 data awal dan 2 data baru). Baris Kategori dan info khusus berbeda untuk tiap jenis donor, hasil dari method getKategori() dan getInfoKhusus() yang di-override: Donor Baru menampilkan *Sumber Informasi*, Donor Tetap menampilkan *Nomor Kartu* dan *Jumlah Donasi*.

<img width="428" height="1065" alt="image" src="https://github.com/user-attachments/assets/8a1b1390-989a-4115-9306-827d57d3f3b0" />

**Gambar 6.** Update donor ID 4: nama menjadi "Aidil Fadilah" dan golongan darah menjadi B. Update memakai perbaruiData() pada objek yang sudah ada, sehingga validasi tetap berlaku.

<img width="621" height="985" alt="image" src="https://github.com/user-attachments/assets/6d81939c-163e-41d6-a69e-1b8d8033b264" />

**Gambar 7.** Menghapus donor ID 3 (Fadlan). Program meminta konfirmasi y/n terlebih dahulu, lalu menampilkan jumlah riwayat donasi yang ikut terhapus (0 data).

<img width="345" height="710" alt="image" src="https://github.com/user-attachments/assets/994200df-ac71-43a4-a0f6-81f07dfdfd3c" />

**Gambar 8.** Daftar donor setelah penghapusan: donor ID 3 sudah hilang, dan data ID 4 sudah menggunakan nama terbaru.

<img width="406" height="284" alt="image" src="https://github.com/user-attachments/assets/afc9c009-5c4a-4fcb-bcf2-bb7eacdfee5e" />

**Gambar 9.** Mencari donor berdasarkan potongan nama ("Aidil F"). Pencarian tidak peka huruf besar/kecil dan menggunakan versi cariDonor(String) (overloading).

<img width="434" height="244" alt="image" src="https://github.com/user-attachments/assets/e982a120-e985-4f18-b78c-10d1a4a4ccd8" />

**Gambar 10.** Kembali ke Menu Utama setelah selesai mengelola data donor.

---

### 7.2 Kelola Data Donasi

<img width="438" height="171" alt="image" src="https://github.com/user-attachments/assets/f95fe8fd-ddc4-43b8-a47c-64e5c2987ce7" />

**Gambar 11.** Menu utama sebelum masuk ke pengelolaan data donasi.

<img width="303" height="196" alt="image" src="https://github.com/user-attachments/assets/d2aa4db5-f69d-4469-8b34-9ed2afed393f" />

**Gambar 12.** Submenu Kelola Data Donasi dengan empat pilihan: Catat, Lihat Semua, Update, dan Hapus Donasi, serta opsi kembali ke Menu Utama.

<img width="375" height="991" alt="image" src="https://github.com/user-attachments/assets/d4b6ff51-db3d-47d8-a16d-cdc2e8d7d63a" />

**Gambar 13.** Mencatat donasi baru untuk donor ID 4 pada tanggal 2026-01-20 sebanyak 2 kantong. Program menampilkan daftar donor lebih dulu, lalu menghasilkan riwayat donasi dengan ID 2. Karena donor ID 4 adalah Donor Tetap, jumlah donasinya ikut bertambah lewat catatDonasi() yang di-override.

<img width="321" height="503" alt="image" src="https://github.com/user-attachments/assets/e6e90095-6500-41cc-9d29-d1ed1cdd3fd6" />

**Gambar 14.** Menampilkan seluruh riwayat donasi: data awal (ID 1 milik donor ID 2) dan donasi baru (ID 2 milik donor ID 4).

<img width="435" height="544" alt="image" src="https://github.com/user-attachments/assets/b5b94c30-4a69-4416-afe7-c27c38f0bb84" />

**Gambar 15.** Update donasi ID 1: tanggal diubah menjadi 2026-01-16. Tanggal divalidasi memakai LocalDate, sehingga format yang salah ditolak.

<img width="456" height="384" alt="image" src="https://github.com/user-attachments/assets/b921cba7-62cf-40d1-80b1-0e81c02c3e5c" />

**Gambar 16.** Menghapus donasi ID 2 dengan konfirmasi y/n sebelum data benar-benar dihapus.

<img width="321" height="181" alt="image" src="https://github.com/user-attachments/assets/120c26ef-49f0-4a94-8f44-c414f9d80405" />

**Gambar 17.** Daftar donasi setelah penghapusan: tersisa 1 data (ID 1).

<img width="434" height="246" alt="image" src="https://github.com/user-attachments/assets/32caa226-f539-4472-9367-a96574ce9aa7" />

**Gambar 18.** Kembali ke Menu Utama setelah selesai mengelola data donasi.

---

### 7.3 Lihat Ringkasan dan Keluar

<img width="440" height="340" alt="image" src="https://github.com/user-attachments/assets/da5d9745-0590-4072-9375-5a749beebd26" />

**Gambar 19.** Ringkasan data: total donor terdaftar (3), total donasi tercatat (1), dan total kantong darah (1), sesuai kondisi data setelah seluruh operasi CRUD sebelumnya.

<img width="440" height="340" alt="image" src="https://github.com/user-attachments/assets/10bbef11-b2d4-4737-9418-5897be731d52" />

**Gambar 20.** Program keluar setelah memilih menu 0, ditandai pesan "Terima kasih telah menggunakan Sistem Donasi Darah." Baris BUILD SUCCESS adalah output Maven yang menandakan program berjalan sampai selesai.

---
