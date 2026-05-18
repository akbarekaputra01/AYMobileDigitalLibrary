package com.example.aymobiledigitallibrary.data

object LibraryRepository {
    private const val IMAGE_PATH = "file:///android_asset/BookCovers/"

    val items =
            listOf(
                    // URUTAN SESUAI DAFTAR FILE GOOGLE DRIVE (A - Z)
                    LibraryItem(
                            "B01",
                            "Alice's Adventures in Wonderland",
                            "Lewis Carroll",
                            "1865",
                            "Fiction",
                            "${IMAGE_PATH}alice.png"
                    ),
                    LibraryItem(
                            "B02",
                            "King Arthur and His Knights",
                            "Howard Pyle",
                            "1903",
                            "Mythology & Fiction",
                            "${IMAGE_PATH}arthur.png"
                    ),
                    LibraryItem(
                            "B03",
                            "Big Hero 6",
                            "Disney Press",
                            "2014",
                            "Sci-Fi & Adventure",
                            "${IMAGE_PATH}bighero6.jpg"
                    ),
                    LibraryItem(
                            "B04",
                            "A Christmas Carol",
                            "Charles Dickens",
                            "1843",
                            "Fiction",
                            "${IMAGE_PATH}christmas.png"
                    ),
                    LibraryItem(
                            "B05",
                            "Cinderella",
                            "Charles Perrault",
                            "1697",
                            "Children's Literature",
                            "${IMAGE_PATH}Cinderella.png" // Menggunakan 'C' besar sesuai gambar
                    ),
                    LibraryItem(
                            "B06",
                            "The Hundred and One Dalmatians",
                            "Dodie Smith",
                            "1956",
                            "Children's Fiction",
                            "${IMAGE_PATH}dalmation.png"
                    ),
                    LibraryItem(
                            "B07",
                            "Freaky Friday",
                            "Mary Rodgers",
                            "1972",
                            "Comedy & Fiction",
                            "${IMAGE_PATH}freakyfriday.png"
                    ),
                    LibraryItem(
                            "B08",
                            "The Frog Princess",
                            "E.D. Baker",
                            "2002",
                            "Fantasy",
                            "${IMAGE_PATH}frog princess.png" // Menggunakan spasi biasa, bukan
                            // underscore (_)
                            ),
                    LibraryItem(
                            "B09",
                            "The Epic of Gilgamesh",
                            "Ancient Mesopotamian Authors",
                            "2003",
                            "World Literature",
                            "${IMAGE_PATH}gilgamesh.png"
                    ),
                    LibraryItem(
                            "B10",
                            "Heracles",
                            "Greek Mythological Records",
                            "2022",
                            "Mythology",
                            "${IMAGE_PATH}heracles.png"
                    ),
                    LibraryItem(
                            "B11",
                            "Holes",
                            "Louis Sachar",
                            "1998",
                            "Young Adult Fiction",
                            "${IMAGE_PATH}holes.png"
                    ),
                    LibraryItem(
                            "B12",
                            "The Hunchback of Notre-Dame",
                            "Victor Hugo",
                            "1831",
                            "Historical Fiction",
                            "${IMAGE_PATH}hunchbac.jpg"
                    ),
                    LibraryItem(
                            "B13",
                            "The Incredible Journey",
                            "Sheila Burnford",
                            "1961",
                            "Adventure",
                            "${IMAGE_PATH}incredible journey.png" // FIX: Menggunakan spasi sesuai
                            // gambar list
                            ),
                    LibraryItem(
                            "B14",
                            "The Jungle Book",
                            "Rudyard Kipling",
                            "1894",
                            "Children's Fiction",
                            "${IMAGE_PATH}jungle.png"
                    ),
                    LibraryItem(
                            "B15",
                            "Lottie and Lisa",
                            "Erich Kästner",
                            "1949",
                            "Children's Fiction",
                            "${IMAGE_PATH}lottie&lisa.png"
                    ),
                    LibraryItem(
                            "B16",
                            "Mary Poppins",
                            "P.L. Travers",
                            "1934",
                            "Fantasy",
                            "${IMAGE_PATH}marypopping.jpg"
                    ),
                    LibraryItem(
                            "B17",
                            "Medusa: A Chronicle of Olive and Shadow",
                            "Mythological Adaptation",
                            "2024",
                            "Mythology",
                            "${IMAGE_PATH}medusa.png"
                    ),
                    LibraryItem(
                            "B18",
                            "Musashi: A Saga of Sword and Spirit",
                            "Eiji Yoshikawa",
                            "1935",
                            "Biographical Fiction",
                            "${IMAGE_PATH}musashi.png"
                    ),
                    LibraryItem(
                            "B19",
                            "The Nutcracker and the Mouse King",
                            "E.T.A. Hoffmann",
                            "1816",
                            "Fantasy",
                            "${IMAGE_PATH}nutcracker.png"
                    ),
                    LibraryItem(
                            "B20",
                            "The Adventures of Pinocchio",
                            "Carlo Collodi",
                            "1883",
                            "Children's Fiction",
                            "${IMAGE_PATH}pinocchio.jpg"
                    ),
                    LibraryItem(
                            "B21",
                            "Rapunzel",
                            "Brothers Grimm",
                            "1812",
                            "Fairy Tale",
                            "${IMAGE_PATH}rapunzel.png"
                    ),
                    LibraryItem(
                            "B22",
                            "The Merry Adventures of Robin Hood",
                            "Howard Pyle",
                            "1883",
                            "Folklore",
                            "${IMAGE_PATH}ROBINHOOD.png" // Sesuai gambar: HURUF BESAR SEMUA
                    ),
                    LibraryItem(
                            "B23",
                            "Who Censored Roger Rabbit?",
                            "Gary K. Wolf",
                            "1981",
                            "Mystery & Comedy",
                            "${IMAGE_PATH}rogerrabbit.png"
                    ),
                    LibraryItem(
                            "B24",
                            "Sleeping Beauty",
                            "Charles Perrault",
                            "1697",
                            "Fairy Tale",
                            "${IMAGE_PATH}sleeping beauty.png" // Sesuai gambar: huruf kecil semua
                            // dengan spasi
                            ),
                    LibraryItem(
                            "B25",
                            "Snow Queen",
                            "Hans Christian Andersen",
                            "1844",
                            "Fairy Tale",
                            "${IMAGE_PATH}Snow Queen.png" // Sesuai gambar: 'S' dan 'Q' besar dengan
                            // spasi
                            ),
                    LibraryItem(
                            "B26",
                            "Snow White",
                            "Brothers Grimm",
                            "1812",
                            "Fairy Tale",
                            "${IMAGE_PATH}snow white.png" // Sesuai gambar: huruf kecil semua dengan
                            // spasi
                            ),
                    LibraryItem(
                            "B27",
                            "Tarzan of the Apes",
                            "Edgar Rice Burroughs",
                            "1912",
                            "Adventure",
                            "${IMAGE_PATH}tarzan.png"
                    ),
                    LibraryItem(
                            "B28",
                            "Bridge to Terabithia",
                            "Katherine Paterson",
                            "1977",
                            "Drama & Fiction",
                            "${IMAGE_PATH}terabithia.png"
                    ),
                    LibraryItem(
                            "B29",
                            "The Little Mermaid",
                            "Hans Christian Andersen",
                            "1837",
                            "Fairy Tale",
                            "${IMAGE_PATH}The Little Mermaid.png" // FIX: Nama file lengkap sesuai
                            // list Drive Anda
                            ),
                    LibraryItem(
                            "B30",
                            "Tuck Everlasting",
                            "Natalie Babbitt",
                            "1975",
                            "Fantasy Fiction",
                            "${IMAGE_PATH}tuckeverlasting.png"
                    )
            )
}
