-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: njfp
-- ------------------------------------------------------
-- Server version	5.5.24-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `UserConnection`
--

DROP TABLE IF EXISTS `UserConnection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserConnection` (
  `connection_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accessToken` varchar(255) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(255) DEFAULT NULL,
  `providerId` varchar(255) NOT NULL,
  `providerUserId` varchar(255) NOT NULL,
  `rank` int(11) DEFAULT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `userId` varchar(255) NOT NULL,
  PRIMARY KEY (`connection_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserConnection`
--

LOCK TABLES `UserConnection` WRITE;
/*!40000 ALTER TABLE `UserConnection` DISABLE KEYS */;
INSERT INTO `UserConnection` VALUES (1,'404e0e2e-c540-4662-8a32-f2df0344160c','404e0e2e-c540-4662-8a32-f2df0344160c',NULL,NULL,NULL,'springSocialSecurity','Mark Martinez',1,NULL,NULL,'Mark Martinez'),(2,'AAAD1ZBSzyHMsBAIZAXA6fB1QKSrXosguwlIZCj5zTkFMQMaSqkKV1sYfwkO7o4vBKDZBRpKve98o5L0E9u5uANXTF2Gb9LRpKo3jZCjYccwZDZD','mark.martinez.986',1352968752121,'http://graph.facebook.com/534053654/picture','http://facebook.com/profile.php?id=534053654','facebook','534053654',1,NULL,NULL,'Mark Martinez'),(3,'d39ecbbc-1b88-49a0-9e3e-948c8d668356','d39ecbbc-1b88-49a0-9e3e-948c8d668356',NULL,NULL,NULL,'springSocialSecurity','Jesus Christ',1,NULL,NULL,'Jesus Christ'),(4,'AAAD1ZBSzyHMsBAB1sQB4ZAOv4l4DSZBm0p8tVIKt0Fi0Lw15jmETgUo81SqXjrsTGwSN6JmQnb0OrZAFBtAZBGEnOgzhlVKypHkIMEC2ZCxwZDZD','williamlane.craig.7',1352968934514,'http://graph.facebook.com/100004037722317/picture','http://facebook.com/profile.php?id=100004037722317','facebook','100004037722317',1,NULL,NULL,'Jesus Christ'),(5,'f98ae793-7e7c-4e26-ace3-27f6a4ada7dc','f98ae793-7e7c-4e26-ace3-27f6a4ada7dc',NULL,NULL,NULL,'springSocialSecurity','carissa.salin',1,NULL,NULL,'carissa.salin'),(6,'AAAD1ZBSzyHMsBABxZC1WSGajM7TRYzoFvfIc6TEOYnh4vTQxu5J3XsGxeNmv6mv0KrUtEdPZAwgOgLSQdblUeukke1DP61WHbvqR7z28wZDZD','carissa.salin',1352324867439,'http://graph.facebook.com/1082618158/picture','http://facebook.com/profile.php?id=1082618158','facebook','1082618158',1,NULL,NULL,'carissa.salin'),(7,'56243a61-1dd2-46ad-90d1-016c8a5c3852','56243a61-1dd2-46ad-90d1-016c8a5c3852',NULL,NULL,NULL,'springSocialSecurity','My Heart is Bleeding Emo Kid',1,NULL,NULL,'My Heart is Bleeding Emo Kid'),(8,'AAAD1ZBSzyHMsBAAGB7VnlFqwPnZAEWKo5jx76spg7ZCVq2eh1WSAEa5TLUrsnx3ou6dWyYmz6Lqnc4bpANL6SHBRvsmV0Qm7WvZBZBwvrjwZDZD','warstormz.dudez',1352971045246,'http://graph.facebook.com/100001382165171/picture','http://facebook.com/profile.php?id=100001382165171','facebook','100001382165171',1,NULL,NULL,'My Heart is Bleeding Emo Kid'),(9,'fa19904a-7ebf-469f-aaa9-f66e216f0b5b','fa19904a-7ebf-469f-aaa9-f66e216f0b5b',NULL,NULL,NULL,'springSocialSecurity','martinezdescent',1,NULL,NULL,'martinezdescent'),(10,'AAAD1ZBSzyHMsBAIDpWiqFJAHYY40QoXCu3OZAk3FmM8yIH6gen09xkiPQKX0DQUGwg7axYGs79ksflWqKvWRTc82tAa26wbcZB2kATMZBAZDZD','martinezdescent',1352857105226,'http://graph.facebook.com/1089289311/picture','http://facebook.com/profile.php?id=1089289311','facebook','1089289311',1,NULL,NULL,'martinezdescent');
/*!40000 ALTER TABLE `UserConnection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessCategories`
--

DROP TABLE IF EXISTS `businessCategories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businessCategories` (
  `categoryId` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=1816 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessCategories`
--

LOCK TABLES `businessCategories` WRITE;
/*!40000 ALTER TABLE `businessCategories` DISABLE KEYS */;
INSERT INTO `businessCategories` VALUES (1,NULL,'abattoirs'),(2,NULL,'abortion clinics'),(3,NULL,'abrasives'),(4,NULL,'access control'),(5,NULL,'accident investigations'),(6,NULL,'accommodation'),(7,NULL,'accommodation agents'),(8,NULL,'accountants'),(9,NULL,'accounting services'),(10,NULL,'acoustics'),(11,NULL,'actuaries'),(12,NULL,'acupuncture'),(13,NULL,'addressing machines'),(14,NULL,'adhesives'),(15,NULL,'adoption'),(16,NULL,'adult education'),(17,NULL,'adult entertainment'),(18,NULL,'adventure sports'),(19,NULL,'advertising'),(20,NULL,'advertising agencies'),(21,NULL,'aerial installations'),(22,NULL,'aerial photography'),(23,NULL,'aerial platforms'),(24,NULL,'after school centres'),(25,NULL,'agricultural consultants'),(26,NULL,'agricultural contractors'),(27,NULL,'agricultural engineers'),(28,NULL,'agricultural machinery'),(29,NULL,'agricultural services'),(30,NULL,'air charter'),(31,NULL,'air compressors'),(32,NULL,'air conditioner repairs'),(33,NULL,'air conditioners'),(34,NULL,'air conditioning'),(35,NULL,'air conditioning consultants'),(36,NULL,'air filters and purification'),(37,NULL,'air filters and purification services'),(38,NULL,'air ticket reservations'),(39,NULL,'aircraft parts'),(40,NULL,'aircraft services'),(41,NULL,'airlines'),(42,NULL,'airport parking'),(43,NULL,'airport transfers'),(44,NULL,'airports'),(45,NULL,'alloys'),(46,NULL,'aluminium products'),(47,NULL,'aluminium services'),(48,NULL,'ambulance services'),(49,NULL,'amusement machines'),(50,NULL,'amusement parks'),(51,NULL,'animal feed'),(52,NULL,'animal rehabilitation'),(53,NULL,'animal welfare societies'),(54,NULL,'animation'),(55,NULL,'anodisers'),(56,NULL,'antique dealers'),(57,NULL,'antique restoration'),(58,NULL,'aquarium and pond supplies'),(59,NULL,'archaeological surveys'),(60,NULL,'archery'),(61,NULL,'architects'),(62,NULL,'armed forces'),(63,NULL,'aromatherapy'),(64,NULL,'art dealers'),(65,NULL,'art galleries'),(66,NULL,'art schools'),(67,NULL,'artificial flowers'),(68,NULL,'artificial surfaces'),(69,NULL,'artist services'),(70,NULL,'artists'),(71,NULL,'artists paints'),(72,NULL,'artists work'),(73,NULL,'arts and crafts'),(74,NULL,'asbestos'),(75,NULL,'asbestos removal'),(76,NULL,'asphalt'),(77,NULL,'asphalt contractors'),(78,NULL,'astrologers'),(79,NULL,'attorneys'),(80,NULL,'auctioneers'),(81,NULL,'audio visual equipment'),(82,NULL,'audio visual servcies'),(83,NULL,'auto electricians'),(84,NULL,'automation systems'),(85,NULL,'aviaries'),(86,NULL,'baby products and services'),(87,NULL,'babysitters'),(88,NULL,'badges'),(89,NULL,'bags'),(90,NULL,'baked products'),(91,NULL,'bakers'),(92,NULL,'bakers supplies'),(93,NULL,'bakery equipment'),(94,NULL,'balustrades'),(95,NULL,'bands'),(96,NULL,'banks'),(97,NULL,'bar code products'),(98,NULL,'bar fitting'),(99,NULL,'bar fixtures'),(100,NULL,'barcoding'),(101,NULL,'baskets'),(102,NULL,'bath enamelling'),(103,NULL,'bathroom design'),(104,NULL,'bathroom fittings'),(105,NULL,'batteries'),(106,NULL,'battery servicing'),(107,NULL,'beads and beadwork'),(108,NULL,'bearing related items'),(109,NULL,'bearings'),(110,NULL,'beauty salons'),(111,NULL,'beauty schools'),(112,NULL,'beauty supplies'),(113,NULL,'bed and breakfast'),(114,NULL,'beds and bedding'),(115,NULL,'beekeepers and bee removals'),(116,NULL,'beekeepers equipment'),(117,NULL,'betting events'),(118,NULL,'bingo'),(119,NULL,'binoculars and telescopes'),(120,NULL,'bins'),(121,NULL,'biokinetics'),(122,NULL,'bird breeders'),(123,NULL,'blacksmiths'),(124,NULL,'blast cleaning'),(125,NULL,'blinds and awnings'),(126,NULL,'blinds and awnings accessories'),(127,NULL,'blood services'),(128,NULL,'boarding kennels'),(129,NULL,'boat accessories'),(130,NULL,'boat builders and repairs'),(131,NULL,'boats'),(132,NULL,'bodyguards'),(133,NULL,'boiler cleaning and repairs'),(134,NULL,'boiler parts'),(135,NULL,'boiler services'),(136,NULL,'bolts'),(137,NULL,'bonsai'),(138,NULL,'bookbinding'),(139,NULL,'bookbinding equipment'),(140,NULL,'bookkeepers'),(141,NULL,'bookmakers'),(142,NULL,'books and magazines'),(143,NULL,'bookseller services'),(144,NULL,'borehole drillers'),(145,NULL,'borehole services'),(146,NULL,'bottle stores'),(147,NULL,'braai equipment'),(148,NULL,'brakes and clutches'),(149,NULL,'brazing'),(150,NULL,'breakdown recovery'),(151,NULL,'breweries'),(152,NULL,'brewing services'),(153,NULL,'brick structures'),(154,NULL,'brickmaking machinery'),(155,NULL,'bricks'),(156,NULL,'bridal wear'),(157,NULL,'broadcasting services'),(158,NULL,'brushes'),(159,NULL,'builders'),(160,NULL,'builders board'),(161,NULL,'builders merchants'),(162,NULL,'building blocks'),(163,NULL,'building cleaning services'),(164,NULL,'building consultants'),(165,NULL,'building machinery'),(166,NULL,'building maintenance'),(167,NULL,'building materials'),(168,NULL,'building products'),(169,NULL,'building services engineering'),(170,NULL,'building societies'),(171,NULL,'built-in units'),(172,NULL,'bus and coach services'),(173,NULL,'bus lines'),(174,NULL,'business centre services'),(175,NULL,'business consultants'),(176,NULL,'business development'),(177,NULL,'business enterprise agencies'),(178,NULL,'business skills training'),(179,NULL,'business software'),(180,NULL,'business specific training'),(181,NULL,'butchers'),(182,NULL,'cabinet makers'),(183,NULL,'cable laying'),(184,NULL,'cablecars'),(185,NULL,'cables'),(186,NULL,'cabling components'),(187,NULL,'cafes and coffee shops'),(188,NULL,'cake makers and decorations'),(189,NULL,'calibration equipment'),(190,NULL,'calibration work'),(191,NULL,'call centre services'),(192,NULL,'calligraphy'),(193,NULL,'camping and caravanning'),(194,NULL,'camping equipment'),(195,NULL,'camps'),(196,NULL,'candle related items'),(197,NULL,'candles'),(198,NULL,'cane furniture'),(199,NULL,'cane furniture materials'),(200,NULL,'canvas products'),(201,NULL,'canvas repairs'),(202,NULL,'car accessories'),(203,NULL,'car audio and entertainment equipment'),(204,NULL,'car body repairs'),(205,NULL,'car dealers services'),(206,NULL,'car electrical parts'),(207,NULL,'car electrics services'),(208,NULL,'car engine tuning and conversion'),(209,NULL,'car hire'),(210,NULL,'car leasing services'),(211,NULL,'car painting services'),(212,NULL,'car paints'),(213,NULL,'car parts'),(214,NULL,'car radiator parts'),(215,NULL,'car security'),(216,NULL,'car sunroofs'),(217,NULL,'car upholstery'),(218,NULL,'car warranties'),(219,NULL,'car washes'),(220,NULL,'car washing equipment'),(221,NULL,'caravan accessories'),(222,NULL,'caravan agents and dealers'),(223,NULL,'caravan and trailer accessories'),(224,NULL,'caravan hire'),(225,NULL,'caravan parks'),(226,NULL,'caravan repairs and service'),(227,NULL,'carbon products'),(228,NULL,'cardboard packaging'),(229,NULL,'career advice services'),(230,NULL,'carpenters and joiners'),(231,NULL,'carpentry and joinery products'),(232,NULL,'carpet accessories'),(233,NULL,'carpet and upholstery cleaners'),(234,NULL,'carpet cleaning equipment'),(235,NULL,'carpet dealers'),(236,NULL,'carpet fitters'),(237,NULL,'carpet fitters equipment'),(238,NULL,'carpet manufacturers'),(239,NULL,'carpet repairs'),(240,NULL,'carports'),(241,NULL,'carton services'),(242,NULL,'cartoonists'),(243,NULL,'cash and carry wholesale'),(244,NULL,'cash handling equipment'),(245,NULL,'cash register equipment'),(246,NULL,'casinos'),(247,NULL,'casting agency'),(248,NULL,'casting materials'),(249,NULL,'casting services'),(250,NULL,'castors and wheels'),(251,NULL,'cat breeders'),(252,NULL,'caterers'),(253,NULL,'catering equipment'),(254,NULL,'catering equipment hire'),(255,NULL,'catering equipment maintenance'),(256,NULL,'catering equipment manufacturers'),(257,NULL,'catering equipment supplier services'),(258,NULL,'cctv services'),(259,NULL,'ceiling services'),(260,NULL,'cellphone hire'),(261,NULL,'cellphone repairs'),(262,NULL,'cellphones'),(263,NULL,'cement manufacturers'),(264,NULL,'ceramic products'),(265,NULL,'ceramic products and services'),(266,NULL,'ceramic services'),(267,NULL,'chain suppliers'),(268,NULL,'chain suppliers services'),(269,NULL,'chairs and seating'),(270,NULL,'chambers of commerce'),(271,NULL,'charitable services'),(272,NULL,'chauffeur services'),(273,NULL,'chemical engineers'),(274,NULL,'chemical manufacturing'),(275,NULL,'chemical products'),(276,NULL,'chemical suppliers'),(277,NULL,'chicken supplies'),(278,NULL,'childcare'),(279,NULL,'childrens clothing'),(280,NULL,'chimney builders and repairs'),(281,NULL,'chimney sweeps'),(282,NULL,'china and glassware'),(283,NULL,'chiropodists and podiatrists'),(284,NULL,'chiropractors'),(285,NULL,'christmas trees and decorations'),(286,NULL,'church craftsmen and restorers'),(287,NULL,'cigarettes'),(288,NULL,'cinemas'),(289,NULL,'civil engineering work'),(290,NULL,'civil engineers'),(291,NULL,'cleaning equipment'),(292,NULL,'cleaning materials'),(293,NULL,'cleaning staff'),(294,NULL,'cleaning systems'),(295,NULL,'climbing equipment'),(296,NULL,'clinics'),(297,NULL,'clock and watch services'),(298,NULL,'clothes repairs and alterations'),(299,NULL,'clothes shops'),(300,NULL,'clothing hire'),(301,NULL,'clothing manufacturers'),(302,NULL,'clothing services and supplies'),(303,NULL,'clubs'),(304,NULL,'clubs and associations'),(305,NULL,'co-operative societies'),(306,NULL,'coach body builders'),(307,NULL,'coaching and mentoring'),(308,NULL,'coaching and mentoring specialisms'),(309,NULL,'coal and solid fuel products'),(310,NULL,'coating services'),(311,NULL,'coffee machines'),(312,NULL,'coffee suppliers'),(313,NULL,'coffins'),(314,NULL,'coin and medal dealers'),(315,NULL,'coin counters'),(316,NULL,'coins'),(317,NULL,'cold storage services'),(318,NULL,'collectors items'),(319,NULL,'colleges'),(320,NULL,'combustion engineers'),(321,NULL,'commercial artists'),(322,NULL,'commercial cleaning'),(323,NULL,'commercial food processors'),(324,NULL,'commercial photographers'),(325,NULL,'commercial refrigeration equipment'),(326,NULL,'commercial vehicle dealers'),(327,NULL,'commercial vehicle hire'),(328,NULL,'commercial waste disposal'),(329,NULL,'commercials'),(330,NULL,'community centres'),(331,NULL,'community organisations'),(332,NULL,'community support'),(333,NULL,'company formation and registration services'),(334,NULL,'computer aided design'),(335,NULL,'computer and telephone cleaning'),(336,NULL,'computer consultancy'),(337,NULL,'computer leasing'),(338,NULL,'computer maintenance'),(339,NULL,'computer networking and cabling'),(340,NULL,'computer peripherals'),(341,NULL,'computer schools'),(342,NULL,'computer security tools'),(343,NULL,'computer security work'),(344,NULL,'computer services'),(345,NULL,'computer services and solutions'),(346,NULL,'computer software development'),(347,NULL,'computer software sales'),(348,NULL,'computer supplies'),(349,NULL,'computer systems'),(350,NULL,'concierge services'),(351,NULL,'concrete contractors'),(352,NULL,'concrete engineers'),(353,NULL,'concrete mixers'),(354,NULL,'concrete pipes'),(355,NULL,'concrete products'),(356,NULL,'concrete pumping'),(357,NULL,'condensation control'),(358,NULL,'confectionery'),(359,NULL,'conference venues'),(360,NULL,'conservatories'),(361,NULL,'consulting engineer services'),(362,NULL,'consulting engineers'),(363,NULL,'consumer organisations'),(364,NULL,'contact lenses'),(365,NULL,'container services'),(366,NULL,'container supplies'),(367,NULL,'contract packers'),(368,NULL,'control panel manufacturing services'),(369,NULL,'control panels'),(370,NULL,'control panels and equipment'),(371,NULL,'conveyor belting services'),(372,NULL,'conveyor system services'),(373,NULL,'conveyor systems and belting'),(374,NULL,'cookery schools'),(375,NULL,'cooking oils'),(376,NULL,'cooking utensils'),(377,NULL,'coolants'),(378,NULL,'cooling towers'),(379,NULL,'copying and duplicating services'),(380,NULL,'copying services'),(381,NULL,'corporate events'),(382,NULL,'cosmetics'),(383,NULL,'cosmetics and toiletries'),(384,NULL,'cosmetics ingredients'),(385,NULL,'cosmetics services'),(386,NULL,'counselling and advice'),(387,NULL,'courier services'),(388,NULL,'courts'),(389,NULL,'crane hire'),(390,NULL,'cranes'),(391,NULL,'creches'),(392,NULL,'credit and finance companies'),(393,NULL,'credit information'),(394,NULL,'credit management'),(395,NULL,'crematoria and cemeteries'),(396,NULL,'cricket lessons'),(397,NULL,'cruises'),(398,NULL,'curtain accessories'),(399,NULL,'curtain cleaners'),(400,NULL,'curtains and soft furnishings'),(401,NULL,'cushions'),(402,NULL,'customising'),(403,NULL,'cutlery'),(404,NULL,'cutting services'),(405,NULL,'cutting tools'),(406,NULL,'cvs'),(407,NULL,'cycle shops and repairs'),(408,NULL,'dairies'),(409,NULL,'dairy equipment and supplies'),(410,NULL,'dairy produce'),(411,NULL,'dairy products'),(412,NULL,'dairy services'),(413,NULL,'damp proofing'),(414,NULL,'dancewear'),(415,NULL,'dancing schools'),(416,NULL,'data communication system services'),(417,NULL,'data information specialisms'),(418,NULL,'data recovery'),(419,NULL,'data services'),(420,NULL,'debt adjustment and management'),(421,NULL,'debt advice and counselling'),(422,NULL,'debt collectors'),(423,NULL,'decking'),(424,NULL,'decorating supplies'),(425,NULL,'delicatessens'),(426,NULL,'delicatessens speciality goods'),(427,NULL,'delivery and collections'),(428,NULL,'demolition'),(429,NULL,'demolition services'),(430,NULL,'dental equipment'),(431,NULL,'dental practices'),(432,NULL,'dental treatment'),(433,NULL,'dentists'),(434,NULL,'department stores'),(435,NULL,'design and development engineer services'),(436,NULL,'design consultants services'),(437,NULL,'design work'),(438,NULL,'desk top publishing'),(439,NULL,'detective agencies'),(440,NULL,'detectors'),(441,NULL,'development organisations'),(442,NULL,'diagnostic equipment'),(443,NULL,'dictating machines services'),(444,NULL,'diesel engineers'),(445,NULL,'diesel fuel injection'),(446,NULL,'diesel repairs'),(447,NULL,'dieting and weight control'),(448,NULL,'digital imaging'),(449,NULL,'digital imaging services'),(450,NULL,'direct mail'),(451,NULL,'disability aids and equipment'),(452,NULL,'disability information and services'),(453,NULL,'disabled driving equipment and services'),(454,NULL,'disco equipment'),(455,NULL,'disco equipment services'),(456,NULL,'discount stores'),(457,NULL,'dishwashers'),(458,NULL,'dishwashing machine services'),(459,NULL,'display contractors'),(460,NULL,'display makers'),(461,NULL,'disposable products'),(462,NULL,'distillers'),(463,NULL,'distribution services'),(464,NULL,'divers'),(465,NULL,'divers equipment'),(466,NULL,'diving schools and equipment'),(467,NULL,'diy stores products'),(468,NULL,'dna testing'),(469,NULL,'doctors'),(470,NULL,'document management'),(471,NULL,'dog and cat grooming'),(472,NULL,'dog breeders'),(473,NULL,'dog grooming services'),(474,NULL,'dog supplies'),(475,NULL,'dog training'),(476,NULL,'doll and teddy hospital services'),(477,NULL,'domestic cleaning'),(478,NULL,'domestic maintenance and repair services'),(479,NULL,'domestic maintenance services'),(480,NULL,'door and window fittings'),(481,NULL,'door manufacturers'),(482,NULL,'doors'),(483,NULL,'drain and pipe work'),(484,NULL,'drain cleaning equipment'),(485,NULL,'drains and pipe cleaning'),(486,NULL,'draughting and design work'),(487,NULL,'drawing equipment'),(488,NULL,'drawing office supplies'),(489,NULL,'dressmaker services'),(490,NULL,'dressmakers'),(491,NULL,'drilling contractor equipment'),(492,NULL,'drilling contractors'),(493,NULL,'drilling contractors services'),(494,NULL,'driving schools'),(495,NULL,'drum dealers'),(496,NULL,'dry cleaners'),(497,NULL,'dry cleaning services'),(498,NULL,'dry foods'),(499,NULL,'ducting and ductwork'),(500,NULL,'duplicating services'),(501,NULL,'dust extraction and ventilation'),(502,NULL,'dvd and cd services'),(503,NULL,'editorial services'),(504,NULL,'educational equipment and supplies'),(505,NULL,'educational services'),(506,NULL,'egg suppliers'),(507,NULL,'electric heating'),(508,NULL,'electric motor parts'),(509,NULL,'electric motor services'),(510,NULL,'electric motors'),(511,NULL,'electric vehicles'),(512,NULL,'electrical appliance repairs and parts'),(513,NULL,'electrical appliances'),(514,NULL,'electrical components'),(515,NULL,'electrical engineers'),(516,NULL,'electrical engineers services'),(517,NULL,'electrical heating equipment'),(518,NULL,'electrical inspecting and testing'),(519,NULL,'electrical supplies'),(520,NULL,'electrical supplies and services'),(521,NULL,'electrical testing services'),(522,NULL,'electrical wiring'),(523,NULL,'electricians'),(524,NULL,'electricity suppliers'),(525,NULL,'electrolysis and laser hair removal'),(526,NULL,'electronic component services'),(527,NULL,'electronic components'),(528,NULL,'electronic engineers'),(529,NULL,'electronic equipment'),(530,NULL,'electronic systems'),(531,NULL,'electroplaters and metal finishers'),(532,NULL,'electroplating materials'),(533,NULL,'electroplating services'),(534,NULL,'embassies and consulates'),(535,NULL,'embroidery'),(536,NULL,'emergency lighting systems'),(537,NULL,'emergency services'),(538,NULL,'employment agency services'),(539,NULL,'employment agency specialisms'),(540,NULL,'employment law'),(541,NULL,'enamellers'),(542,NULL,'energy conservation'),(543,NULL,'energy consultants'),(544,NULL,'engine equipment'),(545,NULL,'engine manufacturers and suppliers'),(546,NULL,'engine reconditioning'),(547,NULL,'engine repairs and services'),(548,NULL,'engineering machine shops'),(549,NULL,'engineering services'),(550,NULL,'engineers'),(551,NULL,'engineers supplies'),(552,NULL,'engines'),(553,NULL,'engravers supplies'),(554,NULL,'engraving'),(555,NULL,'entertainers'),(556,NULL,'entertainment agencies'),(557,NULL,'entertainment equipment'),(558,NULL,'entertainment shows'),(559,NULL,'envelope services'),(560,NULL,'environmental consultant services'),(561,NULL,'environmental consultants'),(562,NULL,'epos equipment and accessories'),(563,NULL,'essential oils'),(564,NULL,'estate agents'),(565,NULL,'event services'),(566,NULL,'event tickets'),(567,NULL,'evidence gathering equipment'),(568,NULL,'exercise equipment'),(569,NULL,'exhaust dealers products'),(570,NULL,'exhaust dealers services'),(571,NULL,'exhaust system services'),(572,NULL,'exhaust systems'),(573,NULL,'exhibition services'),(574,NULL,'exhibition stands'),(575,NULL,'exhibitions'),(576,NULL,'export agents services'),(577,NULL,'external building work'),(578,NULL,'fabric retailer services'),(579,NULL,'fabric shops'),(580,NULL,'fabricated steel products'),(581,NULL,'fabrication services'),(582,NULL,'fabrics'),(583,NULL,'facilities management services'),(584,NULL,'factoring and invoice discounting'),(585,NULL,'factory shops'),(586,NULL,'family planning clinics'),(587,NULL,'fan and blower services'),(588,NULL,'fancy goods'),(589,NULL,'fancy goods services'),(590,NULL,'fans and blowers'),(591,NULL,'farm facilities and services'),(592,NULL,'farm stalls'),(593,NULL,'farmers'),(594,NULL,'farrier services'),(595,NULL,'fascias'),(596,NULL,'fashion accessories'),(597,NULL,'fashion designer services'),(598,NULL,'fashion designers'),(599,NULL,'fast food'),(600,NULL,'fencing'),(601,NULL,'fencing materials'),(602,NULL,'feng shui'),(603,NULL,'ferry services'),(604,NULL,'fertilizers and compost'),(605,NULL,'fibre extrusion equipment'),(606,NULL,'fibre optic installations'),(607,NULL,'fibres'),(608,NULL,'film and video production equipment'),(609,NULL,'film and video production services'),(610,NULL,'filter manufacturers'),(611,NULL,'filters'),(612,NULL,'finance'),(613,NULL,'finance brokers'),(614,NULL,'financial advice'),(615,NULL,'fire alarms'),(616,NULL,'fire brigade'),(617,NULL,'fire escapes'),(618,NULL,'fire extinguishers'),(619,NULL,'fire protection engineers'),(620,NULL,'fire safety services'),(621,NULL,'fireplace accessories'),(622,NULL,'fireplaces'),(623,NULL,'fireproofing'),(624,NULL,'fireworks'),(625,NULL,'first aid supplies'),(626,NULL,'first aid training'),(627,NULL,'fish and chip shops'),(628,NULL,'fish farms'),(629,NULL,'fish merchants'),(630,NULL,'fishing'),(631,NULL,'fishing companies'),(632,NULL,'fishing tackle'),(633,NULL,'fishmongers'),(634,NULL,'fixings and fastenings'),(635,NULL,'flags and banners'),(636,NULL,'flood damage'),(637,NULL,'floor coverings'),(638,NULL,'floor treatments'),(639,NULL,'flooring equipment'),(640,NULL,'flooring materials'),(641,NULL,'flooring work'),(642,NULL,'floral display services'),(643,NULL,'florists'),(644,NULL,'flowers'),(645,NULL,'flying schools'),(646,NULL,'foam products'),(647,NULL,'food additives'),(648,NULL,'food and drink catering supplies'),(649,NULL,'food and drink manufacturing'),(650,NULL,'food flavourings'),(651,NULL,'food manufacturers'),(652,NULL,'food manufacturers services'),(653,NULL,'food processing equipment'),(654,NULL,'food processors'),(655,NULL,'food suppliers'),(656,NULL,'footwear manufacturers'),(657,NULL,'footwear suppliers'),(658,NULL,'foreign exchange'),(659,NULL,'forensic services'),(660,NULL,'forestry'),(661,NULL,'forgings'),(662,NULL,'fork lift truck parts and repairs'),(663,NULL,'fork lift truck services'),(664,NULL,'fork lift trucks'),(665,NULL,'fortune tellers'),(666,NULL,'foundries'),(667,NULL,'franchising'),(668,NULL,'freight containers'),(669,NULL,'freight forwarding'),(670,NULL,'french polishers'),(671,NULL,'fridges and freezer repairs'),(672,NULL,'fridges and freezer retail'),(673,NULL,'frozen food'),(674,NULL,'fruit and vegetables'),(675,NULL,'fruit juice wholesalers'),(676,NULL,'fuel'),(677,NULL,'fuel injection'),(678,NULL,'fumigation'),(679,NULL,'function hire services'),(680,NULL,'fund raising'),(681,NULL,'funeral directors'),(682,NULL,'funeral directors equipment'),(683,NULL,'fur retailers'),(684,NULL,'furniture and furnishings'),(685,NULL,'furniture dealers'),(686,NULL,'furniture hire'),(687,NULL,'furniture manufacturers and designers'),(688,NULL,'furniture removals'),(689,NULL,'furniture restoration'),(690,NULL,'further education'),(691,NULL,'galvanisers'),(692,NULL,'gambling events'),(693,NULL,'game'),(694,NULL,'game breeders'),(695,NULL,'games shops'),(696,NULL,'gaming equipment'),(697,NULL,'garage equipment'),(698,NULL,'garage services'),(699,NULL,'garden buildings'),(700,NULL,'garden furniture'),(701,NULL,'garden machinery'),(702,NULL,'garden maintenance'),(703,NULL,'garden services'),(704,NULL,'garden sheds buildings and fences'),(705,NULL,'garden tools'),(706,NULL,'gas apparatus equipment'),(707,NULL,'gas appliances'),(708,NULL,'gas installers'),(709,NULL,'gas suppliers'),(710,NULL,'gaskets and seals'),(711,NULL,'gates and railings'),(712,NULL,'gear box repairs'),(713,NULL,'gear boxes'),(714,NULL,'gear cutters and makers'),(715,NULL,'gemstones'),(716,NULL,'genealogists'),(717,NULL,'general dealers'),(718,NULL,'general heating equipment'),(719,NULL,'general insurance'),(720,NULL,'generators'),(721,NULL,'generators and accessories'),(722,NULL,'geologists and geophysicists'),(723,NULL,'gift services'),(724,NULL,'gift shops'),(725,NULL,'glace fruit'),(726,NULL,'glass'),(727,NULL,'glass blowers'),(728,NULL,'glass cutting'),(729,NULL,'glass fibre'),(730,NULL,'glass furniture'),(731,NULL,'glass manufacturers'),(732,NULL,'glassware'),(733,NULL,'glaziers'),(734,NULL,'gloves'),(735,NULL,'goldsmiths and silversmiths'),(736,NULL,'golf accessories'),(737,NULL,'golf club services'),(738,NULL,'golf courses'),(739,NULL,'golf driving ranges'),(740,NULL,'government departments'),(741,NULL,'grain services'),(742,NULL,'granite products'),(743,NULL,'graphic designers'),(744,NULL,'greengrocers'),(745,NULL,'greenhouses'),(746,NULL,'greeting cards'),(747,NULL,'grinders'),(748,NULL,'grinding'),(749,NULL,'groundwork contractors'),(750,NULL,'guest houses'),(751,NULL,'gun dealers'),(752,NULL,'guttering'),(753,NULL,'haberdashery'),(754,NULL,'hair accessories'),(755,NULL,'hair and scalp treatments'),(756,NULL,'hair consultants'),(757,NULL,'hair removal'),(758,NULL,'hairdressers'),(759,NULL,'hairdressing supplies'),(760,NULL,'hairpieces'),(761,NULL,'halls'),(762,NULL,'hampers'),(763,NULL,'handwriting analysis'),(764,NULL,'handyman services'),(765,NULL,'hardware stores'),(766,NULL,'hardware wholesalers'),(767,NULL,'hat shops'),(768,NULL,'health care'),(769,NULL,'health club equipment'),(770,NULL,'health clubs'),(771,NULL,'health food products'),(772,NULL,'health foods'),(773,NULL,'health insurance'),(774,NULL,'health spas and resorts'),(775,NULL,'healthcare services'),(776,NULL,'hearing aid service'),(777,NULL,'hearing aids'),(778,NULL,'heat exchangers'),(779,NULL,'heat treatment'),(780,NULL,'heating'),(781,NULL,'herbalists'),(782,NULL,'hi-fi and audio equipment'),(783,NULL,'hi-fi and audio repairs'),(784,NULL,'hides'),(785,NULL,'hire equipment'),(786,NULL,'holiday accommodation'),(787,NULL,'home improvements'),(788,NULL,'home theatre systems'),(789,NULL,'homeopaths'),(790,NULL,'honey'),(791,NULL,'horse carriages'),(792,NULL,'horse trainers'),(793,NULL,'horse transport'),(794,NULL,'hose and accessories'),(795,NULL,'hoses flexible'),(796,NULL,'hosiery'),(797,NULL,'hospices'),(798,NULL,'hospitals'),(799,NULL,'hostels'),(800,NULL,'hot air balloons'),(801,NULL,'hot tubs and spas'),(802,NULL,'hotel accommodation'),(803,NULL,'hotel booking agents'),(804,NULL,'house and pet sitting'),(805,NULL,'household appliances'),(806,NULL,'human resource consultants'),(807,NULL,'hydraulic engineers'),(808,NULL,'hydraulic services'),(809,NULL,'hydroponics'),(810,NULL,'hygiene services'),(811,NULL,'hypnotherapists'),(812,NULL,'ice cream equipment'),(813,NULL,'ice cream products'),(814,NULL,'ice cream suppliers'),(815,NULL,'ice suppliers'),(816,NULL,'identification cards'),(817,NULL,'image consultants'),(818,NULL,'immigration services'),(819,NULL,'import agents'),(820,NULL,'incinerator services'),(821,NULL,'indoor plants'),(822,NULL,'industrial and commercial consultants'),(823,NULL,'industrial and medical gases'),(824,NULL,'industrial cleaning'),(825,NULL,'industrial cleaning equipment'),(826,NULL,'industrial engineers'),(827,NULL,'industrial heating services'),(828,NULL,'industrial protective clothing'),(829,NULL,'industrial sewing machines'),(830,NULL,'industrial supplies'),(831,NULL,'infertility'),(832,NULL,'information'),(833,NULL,'information services'),(834,NULL,'insolvency services'),(835,NULL,'inspecting and testing services'),(836,NULL,'inspection engineers'),(837,NULL,'instrumentation engineers'),(838,NULL,'instrumentation services'),(839,NULL,'insulation installers'),(840,NULL,'insulation materials'),(841,NULL,'insurance'),(842,NULL,'insurance assessors'),(843,NULL,'insurance other'),(844,NULL,'intellectual property'),(845,NULL,'intercom systems'),(846,NULL,'interior design supplies'),(847,NULL,'interior designers'),(848,NULL,'internet cafes'),(849,NULL,'internet services'),(850,NULL,'introduction agencies'),(851,NULL,'investment companies'),(852,NULL,'investment consultants'),(853,NULL,'investment products'),(854,NULL,'iron and steel products'),(855,NULL,'ironing services'),(856,NULL,'irrigation engineers'),(857,NULL,'irrigation equipment'),(858,NULL,'items for delivery and collection'),(859,NULL,'janitorial supplies'),(860,NULL,'jewellers'),(861,NULL,'jewellers services'),(862,NULL,'jewellery services'),(863,NULL,'jewellery supplies'),(864,NULL,'joinery manufacturers'),(865,NULL,'juke boxes'),(866,NULL,'jumping castles'),(867,NULL,'karaoke'),(868,NULL,'karting'),(869,NULL,'karting equipment'),(870,NULL,'keys'),(871,NULL,'kilns'),(872,NULL,'kitchen furniture installations'),(873,NULL,'kitchenware'),(874,NULL,'kite surfing'),(875,NULL,'kites'),(876,NULL,'knitted items'),(877,NULL,'knitting and needlework'),(878,NULL,'knitting patterns'),(879,NULL,'knitwear'),(880,NULL,'label printing'),(881,NULL,'laboratory equipment'),(882,NULL,'laboratory research'),(883,NULL,'ladders'),(884,NULL,'ladies accessories'),(885,NULL,'ladies clothes shops'),(886,NULL,'laminating'),(887,NULL,'land agents'),(888,NULL,'land surveyors'),(889,NULL,'landscape architects'),(890,NULL,'landscapers'),(891,NULL,'language courses'),(892,NULL,'laser applications'),(893,NULL,'laser equipment and services'),(894,NULL,'laser services'),(895,NULL,'launderettes'),(896,NULL,'laundries'),(897,NULL,'laundry equipment'),(898,NULL,'laundry services'),(899,NULL,'lawnmowers'),(900,NULL,'lead products'),(901,NULL,'leasing companies'),(902,NULL,'leather furniture'),(903,NULL,'leather materials'),(904,NULL,'leather suppliers'),(905,NULL,'leather working'),(906,NULL,'leathergoods'),(907,NULL,'legal services'),(908,NULL,'legal specialisms'),(909,NULL,'letting agents'),(910,NULL,'libraries'),(911,NULL,'library services'),(912,NULL,'lift manufacturers'),(913,NULL,'lifting gear'),(914,NULL,'lifting gear services'),(915,NULL,'lifting systems'),(916,NULL,'lifts'),(917,NULL,'light fittings'),(918,NULL,'lighting'),(919,NULL,'lighting consultants'),(920,NULL,'lighting hire'),(921,NULL,'lighting installers'),(922,NULL,'lighting production'),(923,NULL,'lighting products'),(924,NULL,'lightning conductors'),(925,NULL,'lime suppliers'),(926,NULL,'limousine hire'),(927,NULL,'linen hire'),(928,NULL,'linen suppliers'),(929,NULL,'lingerie'),(930,NULL,'literary agents'),(931,NULL,'livestock auctioneers'),(932,NULL,'livestock breeders'),(933,NULL,'loans'),(934,NULL,'lobby groups'),(935,NULL,'local government'),(936,NULL,'lock manufacturers'),(937,NULL,'locksmiths'),(938,NULL,'lofts'),(939,NULL,'lubricanting equipment'),(940,NULL,'lubricants'),(941,NULL,'luggage'),(942,NULL,'machine tool services'),(943,NULL,'machine tools'),(944,NULL,'machinery'),(945,NULL,'machinery manufacturing'),(946,NULL,'machinery repair and reconditioning services'),(947,NULL,'magnets'),(948,NULL,'mail order services'),(949,NULL,'mail room equipment'),(950,NULL,'mailboxes'),(951,NULL,'mailing services'),(952,NULL,'makes of tools'),(953,NULL,'management consultancy services'),(954,NULL,'manufacturers agents'),(955,NULL,'maps'),(956,NULL,'marble'),(957,NULL,'marble service'),(958,NULL,'marinas'),(959,NULL,'marine consultants'),(960,NULL,'marine electronics services'),(961,NULL,'marine engineers'),(962,NULL,'marine engines'),(963,NULL,'marine surveyors'),(964,NULL,'market research'),(965,NULL,'marketing consultants'),(966,NULL,'markets and market equipment'),(967,NULL,'marking equipment'),(968,NULL,'marquee and tent hire'),(969,NULL,'marquee and tent manufacturers'),(970,NULL,'marriage guidance'),(971,NULL,'martial arts'),(972,NULL,'martial arts equipment'),(973,NULL,'massage'),(974,NULL,'massage parlours'),(975,NULL,'materials handling'),(976,NULL,'maternity wear'),(977,NULL,'mats and matting'),(978,NULL,'meat suppliers'),(979,NULL,'mechanical engineers'),(980,NULL,'media'),(981,NULL,'media and communication'),(982,NULL,'mediation'),(983,NULL,'medical equipment'),(984,NULL,'medical insurance'),(985,NULL,'medical supplies'),(986,NULL,'medical supplies and equipment'),(987,NULL,'medical treatment equipment'),(988,NULL,'medical treatments'),(989,NULL,'mens accessories'),(990,NULL,'mens clothes hire'),(991,NULL,'mens clothes shops'),(992,NULL,'metal alloys'),(993,NULL,'metal and metal goods'),(994,NULL,'metal castings'),(995,NULL,'metal furniture'),(996,NULL,'metal furniture repairs'),(997,NULL,'metal heat treatment'),(998,NULL,'metal polishers'),(999,NULL,'metal products'),(1000,NULL,'metal services'),(1001,NULL,'metal spinning and pressing'),(1002,NULL,'metal sprayers and finishers'),(1003,NULL,'metallurgical engineers'),(1004,NULL,'metals'),(1005,NULL,'metalworking'),(1006,NULL,'meter installers'),(1007,NULL,'meter readers'),(1008,NULL,'microwave oven repairs'),(1009,NULL,'milking machinery'),(1010,NULL,'mine tailings'),(1011,NULL,'mineral water'),(1012,NULL,'mining'),(1013,NULL,'mining engineers'),(1014,NULL,'mining equipment'),(1015,NULL,'mining services'),(1016,NULL,'mirrors'),(1017,NULL,'mixing and blending machines'),(1018,NULL,'mobile discos'),(1019,NULL,'model making services'),(1020,NULL,'model shops'),(1021,NULL,'modelling agencies'),(1022,NULL,'mortgages'),(1023,NULL,'mot testing'),(1024,NULL,'motor radiator services'),(1025,NULL,'motor sports'),(1026,NULL,'motorcycle accessories and parts'),(1027,NULL,'motorcycle clothing'),(1028,NULL,'motorcycle hire'),(1029,NULL,'motorcycle repairs and services'),(1030,NULL,'motorcycles'),(1031,NULL,'motoring clubs'),(1032,NULL,'moulding specialties'),(1033,NULL,'moveable buildings'),(1034,NULL,'movie equipment'),(1035,NULL,'multimedia services and solutions'),(1036,NULL,'museums'),(1037,NULL,'music'),(1038,NULL,'music lessons'),(1039,NULL,'music publishing'),(1040,NULL,'music shops'),(1041,NULL,'music storage formats'),(1042,NULL,'music teachers'),(1043,NULL,'music therapy'),(1044,NULL,'musical instrument repairs'),(1045,NULL,'musical instruments'),(1046,NULL,'musicians and composers'),(1047,NULL,'musicians services'),(1048,NULL,'nail bars'),(1049,NULL,'nannies and au pairs'),(1050,NULL,'nature and game parks'),(1051,NULL,'needlework'),(1052,NULL,'nets'),(1053,NULL,'new car dealers'),(1054,NULL,'newsagents'),(1055,NULL,'newspaper and magazine services'),(1056,NULL,'newspapers'),(1057,NULL,'nightwear'),(1058,NULL,'noise and vibration services'),(1059,NULL,'noise control'),(1060,NULL,'nozzles'),(1061,NULL,'number plates'),(1062,NULL,'nurseries'),(1063,NULL,'nursery schools'),(1064,NULL,'nursery supplies'),(1065,NULL,'nurses and care'),(1066,NULL,'nursing and care agency associations'),(1067,NULL,'nursing homes'),(1068,NULL,'nuts and bolts'),(1069,NULL,'occupational health'),(1070,NULL,'occupational therapists'),(1071,NULL,'office equipment repairs'),(1072,NULL,'office equipment services'),(1073,NULL,'office equipment suppliers'),(1074,NULL,'office fitting'),(1075,NULL,'office furniture'),(1076,NULL,'office furniture services'),(1077,NULL,'office premises'),(1078,NULL,'office stationery'),(1079,NULL,'office technical equipment'),(1080,NULL,'offices rental'),(1081,NULL,'oil and gas exploration'),(1082,NULL,'oil and gas field equipment'),(1083,NULL,'oil and gas products'),(1084,NULL,'oil companies'),(1085,NULL,'oil fired heating supplies'),(1086,NULL,'oil fuel distributors'),(1087,NULL,'oil waste disposal'),(1088,NULL,'online services'),(1089,NULL,'optical and precision instruments'),(1090,NULL,'optical goods'),(1091,NULL,'optical goods and supplies'),(1092,NULL,'opticians'),(1093,NULL,'organ builders'),(1094,NULL,'organic foods'),(1095,NULL,'organs'),(1096,NULL,'orthodontists'),(1097,NULL,'orthopaedic goods'),(1098,NULL,'orthopaedic products'),(1099,NULL,'orthoptists'),(1100,NULL,'osteopaths'),(1101,NULL,'outboard motors'),(1102,NULL,'outdoor entertainment and activities'),(1103,NULL,'outdoor pursuits'),(1104,NULL,'outdoor toys'),(1105,NULL,'outdoor wear'),(1106,NULL,'oven builders'),(1107,NULL,'packaging'),(1108,NULL,'packaging containers'),(1109,NULL,'packaging machinery'),(1110,NULL,'packaging materials'),(1111,NULL,'packaging processes'),(1112,NULL,'packing services'),(1113,NULL,'pain management'),(1114,NULL,'paint spraying and mixing equipment'),(1115,NULL,'paint stripping'),(1116,NULL,'paint varnish and lacquer'),(1117,NULL,'painting and decorating'),(1118,NULL,'painting and decorating equipment'),(1119,NULL,'painting contractors'),(1120,NULL,'paints'),(1121,NULL,'pallet trucks'),(1122,NULL,'pallets'),(1123,NULL,'panelling'),(1124,NULL,'paper and board'),(1125,NULL,'paper and packaging'),(1126,NULL,'paper bags'),(1127,NULL,'paper cups'),(1128,NULL,'paper manufacturing'),(1129,NULL,'parachuting and paragliding'),(1130,NULL,'paraffin'),(1131,NULL,'paraffin appliances'),(1132,NULL,'parking services'),(1133,NULL,'parks and gardens'),(1134,NULL,'partitioning'),(1135,NULL,'party balloons'),(1136,NULL,'party goods services'),(1137,NULL,'party planners'),(1138,NULL,'passport offices'),(1139,NULL,'patent attorneys'),(1140,NULL,'patios'),(1141,NULL,'paving'),(1142,NULL,'paving manufacturers'),(1143,NULL,'paving supplies'),(1144,NULL,'pawnbrokers'),(1145,NULL,'payments'),(1146,NULL,'payroll services'),(1147,NULL,'pencils and pens'),(1148,NULL,'pension consultants'),(1149,NULL,'pensions funds'),(1150,NULL,'persian carpets'),(1151,NULL,'personal trainers'),(1152,NULL,'personnel consultants'),(1153,NULL,'pest control'),(1154,NULL,'pest control supplies'),(1155,NULL,'pet foods'),(1156,NULL,'pet funerals'),(1157,NULL,'pet insurance services'),(1158,NULL,'pet services'),(1159,NULL,'pet shops'),(1160,NULL,'pet supplies'),(1161,NULL,'petrol pump installers'),(1162,NULL,'petrol station equipment'),(1163,NULL,'petrol stations'),(1164,NULL,'petroleum products'),(1165,NULL,'pharmaceutical services'),(1166,NULL,'pharmaceutical suppliers'),(1167,NULL,'pharmacies'),(1168,NULL,'photograph libraries'),(1169,NULL,'photograph processing'),(1170,NULL,'photographers'),(1171,NULL,'photographic equipment'),(1172,NULL,'photographic equipment repairs'),(1173,NULL,'photographic processing and printing'),(1174,NULL,'photographic techniques'),(1175,NULL,'physiotherapists'),(1176,NULL,'physiotherapy treatments'),(1177,NULL,'piano tuning and repair'),(1178,NULL,'pianos'),(1179,NULL,'picture framing'),(1180,NULL,'picture framing supplies'),(1181,NULL,'piercing services'),(1182,NULL,'pile driving'),(1183,NULL,'pipe work'),(1184,NULL,'pipes and fittings'),(1185,NULL,'pipes and fittings services'),(1186,NULL,'pipework contractors'),(1187,NULL,'pizza delivery'),(1188,NULL,'places of interest'),(1189,NULL,'plan printing'),(1190,NULL,'planning consultants'),(1191,NULL,'plant and machinery'),(1192,NULL,'plant and machinery hire'),(1193,NULL,'plant and machinery installation'),(1194,NULL,'plant and machinery services'),(1195,NULL,'planting and seeding'),(1196,NULL,'plaster supplies'),(1197,NULL,'plaster ware and mouldings'),(1198,NULL,'plasterers'),(1199,NULL,'plastic containers'),(1200,NULL,'plastic furniture'),(1201,NULL,'plastic injection moulding'),(1202,NULL,'plastic moulders services'),(1203,NULL,'plastic products'),(1204,NULL,'plastic sheeting'),(1205,NULL,'plastic welding'),(1206,NULL,'plastics engineers'),(1207,NULL,'plastics machinery'),(1208,NULL,'plastics manufacturers and supplies'),(1209,NULL,'playground equipment'),(1210,NULL,'plumbers'),(1211,NULL,'plumbing accessories'),(1212,NULL,'plumbing services'),(1213,NULL,'pneumatic control equipment'),(1214,NULL,'pneumatic engineers'),(1215,NULL,'pneumatic tools'),(1216,NULL,'pointing services'),(1217,NULL,'police'),(1218,NULL,'political organisations'),(1219,NULL,'pollution control'),(1220,NULL,'polystyrene'),(1221,NULL,'polystyrene products'),(1222,NULL,'ponds and water features'),(1223,NULL,'port harbour and dock services'),(1224,NULL,'portable toilet accessories'),(1225,NULL,'portable toilets hire'),(1226,NULL,'post office phone and licence services'),(1227,NULL,'post offices'),(1228,NULL,'postcards'),(1229,NULL,'posters'),(1230,NULL,'potato suppliers'),(1231,NULL,'pottery services'),(1232,NULL,'pottery supplies'),(1233,NULL,'poultry farmers'),(1234,NULL,'poultry services'),(1235,NULL,'power tool repairs'),(1236,NULL,'power tools'),(1237,NULL,'prams'),(1238,NULL,'precious metals'),(1239,NULL,'precision engineering services'),(1240,NULL,'precision engineers'),(1241,NULL,'prefabricated buildings'),(1242,NULL,'pregnancy counselling'),(1243,NULL,'press cutting services'),(1244,NULL,'pressure gauges'),(1245,NULL,'print finishers services'),(1246,NULL,'printed circuits'),(1247,NULL,'printed products'),(1248,NULL,'printers'),(1249,NULL,'printers services'),(1250,NULL,'printers supplies'),(1251,NULL,'printing'),(1252,NULL,'printing machinery'),(1253,NULL,'printing services'),(1254,NULL,'printing supplies'),(1255,NULL,'prisons'),(1256,NULL,'private investigators'),(1257,NULL,'private schools'),(1258,NULL,'product designer services'),(1259,NULL,'production services'),(1260,NULL,'profile cutting'),(1261,NULL,'project management services'),(1262,NULL,'promotional'),(1263,NULL,'promotional clothing'),(1264,NULL,'promotional items and incentives'),(1265,NULL,'promotional products'),(1266,NULL,'promotional services'),(1267,NULL,'property development'),(1268,NULL,'property investment'),(1269,NULL,'property management'),(1270,NULL,'protective clothing'),(1271,NULL,'psychologists'),(1272,NULL,'psychotherapy'),(1273,NULL,'psychotherapy treatments'),(1274,NULL,'public opinion surveys'),(1275,NULL,'public relations associations'),(1276,NULL,'public relations consultants'),(1277,NULL,'publishers and publications'),(1278,NULL,'pubs and bars'),(1279,NULL,'pump accessories and equipment'),(1280,NULL,'pump services'),(1281,NULL,'pumps and pumping equipment'),(1282,NULL,'punches'),(1283,NULL,'quad bikes and atvs'),(1284,NULL,'quality assurance'),(1285,NULL,'quality assurance services'),(1286,NULL,'quantity surveyor services'),(1287,NULL,'quantity surveyors'),(1288,NULL,'quarries'),(1289,NULL,'quarry products'),(1290,NULL,'radio communication equipment'),(1291,NULL,'radio navigational equipment'),(1292,NULL,'rail signalling systems'),(1293,NULL,'rail transport'),(1294,NULL,'railway construction'),(1295,NULL,'railway equipment'),(1296,NULL,'railway sleeper products'),(1297,NULL,'rainwear'),(1298,NULL,'rare books'),(1299,NULL,'ready mixed concrete'),(1300,NULL,'record cd and tape retailers'),(1301,NULL,'record cd and tape suppliers'),(1302,NULL,'record shops'),(1303,NULL,'recording equipment'),(1304,NULL,'recording studios'),(1305,NULL,'recruitment consultants'),(1306,NULL,'recycling'),(1307,NULL,'recycling containers'),(1308,NULL,'recycling facilities'),(1309,NULL,'reflexology'),(1310,NULL,'reform schools'),(1311,NULL,'refractory products'),(1312,NULL,'refractory services and suppliers'),(1313,NULL,'refrigerated trucks'),(1314,NULL,'refrigeration engineers'),(1315,NULL,'refrigeration equipment'),(1316,NULL,'refrigeration equipment hire'),(1317,NULL,'refrigeration equipment services'),(1318,NULL,'refrigerator repairs'),(1319,NULL,'rehabilitation'),(1320,NULL,'relief organisations'),(1321,NULL,'religions'),(1322,NULL,'religious buildings'),(1323,NULL,'religious education'),(1324,NULL,'religious groups'),(1325,NULL,'religious organisations'),(1326,NULL,'religious services'),(1327,NULL,'relocation agent services'),(1328,NULL,'reptiles'),(1329,NULL,'researchers'),(1330,NULL,'residential accommodation'),(1331,NULL,'restaurants'),(1332,NULL,'retaining walls'),(1333,NULL,'retirement homes'),(1334,NULL,'riding kit'),(1335,NULL,'riding safety equipment'),(1336,NULL,'riding schools activities'),(1337,NULL,'riding schools services'),(1338,NULL,'ringtone and text services'),(1339,NULL,'road contractors'),(1340,NULL,'road haulage'),(1341,NULL,'road marking'),(1342,NULL,'road marking services'),(1343,NULL,'robotics'),(1344,NULL,'roofing contractors'),(1345,NULL,'roofing materials'),(1346,NULL,'roofing services'),(1347,NULL,'rope access'),(1348,NULL,'rope and twine'),(1349,NULL,'rubber and plastics'),(1350,NULL,'rubber manufacturing services'),(1351,NULL,'rubber products'),(1352,NULL,'rubber stamp accessories'),(1353,NULL,'rubber stamps'),(1354,NULL,'sack and bag materials'),(1355,NULL,'saddlery and harnesses'),(1356,NULL,'safes and vaults'),(1357,NULL,'safety equipment'),(1358,NULL,'safety glass'),(1359,NULL,'safety products'),(1360,NULL,'salt suppliers'),(1361,NULL,'salvage and reclamation'),(1362,NULL,'salvage and reclamation services'),(1363,NULL,'sand and stone'),(1364,NULL,'sandwich shop services'),(1365,NULL,'sanitary hygiene products'),(1366,NULL,'sanitary hygiene services'),(1367,NULL,'sanitary hygiene work'),(1368,NULL,'satellite equipment'),(1369,NULL,'satellite navigation systems'),(1370,NULL,'satellite tv and equipment services'),(1371,NULL,'sauna and steamroom equipment'),(1372,NULL,'sauna and steamroom services'),(1373,NULL,'saw repairs'),(1374,NULL,'sawdust'),(1375,NULL,'sawmills'),(1376,NULL,'saws'),(1377,NULL,'scaffolding equipment'),(1378,NULL,'scaffolding work'),(1379,NULL,'scales and weighing equipment'),(1380,NULL,'scales and weighing equipment services'),(1381,NULL,'schools and college admissions'),(1382,NULL,'schools and colleges'),(1383,NULL,'science consultant services'),(1384,NULL,'scientific apparatus'),(1385,NULL,'scissors'),(1386,NULL,'scrap dealers'),(1387,NULL,'screen printers'),(1388,NULL,'screen printing supplies'),(1389,NULL,'sculptors'),(1390,NULL,'sealing compounds'),(1391,NULL,'sealing services'),(1392,NULL,'secondhand building materials'),(1393,NULL,'secondhand dealers'),(1394,NULL,'secondhand electrical appliances'),(1395,NULL,'secondhand furniture'),(1396,NULL,'secretarial services'),(1397,NULL,'secretarial work and skills'),(1398,NULL,'security equipment'),(1399,NULL,'security seals'),(1400,NULL,'security services'),(1401,NULL,'security services training'),(1402,NULL,'security systems'),(1403,NULL,'seed and grain merchants'),(1404,NULL,'seed products'),(1405,NULL,'seeds and bulbs'),(1406,NULL,'self catering accommodation'),(1407,NULL,'septic tanks'),(1408,NULL,'sewage treatment'),(1409,NULL,'sewing machine services'),(1410,NULL,'sewing machine threads'),(1411,NULL,'sewing machines'),(1412,NULL,'sewing machines and accessories'),(1413,NULL,'sexual counselling and advice'),(1414,NULL,'share dealing services'),(1415,NULL,'sharpening services'),(1416,NULL,'sheet metal'),(1417,NULL,'sheet metal machinery'),(1418,NULL,'sheet metal products'),(1419,NULL,'sheet metal work'),(1420,NULL,'sheet metal work services'),(1421,NULL,'shelving and racking services'),(1422,NULL,'shelving and storage supplies'),(1423,NULL,'ship services and supplies'),(1424,NULL,'shipbrokers'),(1425,NULL,'shipping'),(1426,NULL,'shipping agent services'),(1427,NULL,'shipping and forwarding agents'),(1428,NULL,'shipping companies'),(1429,NULL,'shock absorbers'),(1430,NULL,'shoe care accessories'),(1431,NULL,'shoe makers'),(1432,NULL,'shoe repairs'),(1433,NULL,'shoe shops'),(1434,NULL,'shooting ranges'),(1435,NULL,'shop fitters'),(1436,NULL,'shop fitting suppliers'),(1437,NULL,'shop fittings'),(1438,NULL,'shopping centre services'),(1439,NULL,'shopping centres'),(1440,NULL,'shower equipment'),(1441,NULL,'shredding equipment'),(1442,NULL,'shutter manufacturers'),(1443,NULL,'sign makers'),(1444,NULL,'sign makers services'),(1445,NULL,'sign makers supplies'),(1446,NULL,'sign writers'),(1447,NULL,'silicone items'),(1448,NULL,'silver steel'),(1449,NULL,'site investigation services'),(1450,NULL,'skating and skateboarding'),(1451,NULL,'skiing and snowboarding'),(1452,NULL,'skiing lessons and services'),(1453,NULL,'skip services'),(1454,NULL,'skylights'),(1455,NULL,'slate'),(1456,NULL,'snack bars'),(1457,NULL,'snooker and pool centres'),(1458,NULL,'snooker and pool tables and accessories'),(1459,NULL,'social and welfare services'),(1460,NULL,'social service and welfare organisations'),(1461,NULL,'social services'),(1462,NULL,'soft drink suppliers'),(1463,NULL,'soft drinks'),(1464,NULL,'solar energy'),(1465,NULL,'solar energy equipment and components'),(1466,NULL,'soldering brazing and bonding services'),(1467,NULL,'sound equipment'),(1468,NULL,'sound equipment services'),(1469,NULL,'sound equipment systems'),(1470,NULL,'sound recording services'),(1471,NULL,'special schools and colleges'),(1472,NULL,'specialist clothes shops'),(1473,NULL,'specialist printing services'),(1474,NULL,'specific sports training'),(1475,NULL,'speech and drama services'),(1476,NULL,'speech specialisms'),(1477,NULL,'speech therapists'),(1478,NULL,'sports clothing and footwear'),(1479,NULL,'sports clubs and associations'),(1480,NULL,'sports equipment'),(1481,NULL,'sports equipment and services'),(1482,NULL,'sports equipment suppliers'),(1483,NULL,'sports facilities'),(1484,NULL,'sports ground contractor services'),(1485,NULL,'sports ground contractors'),(1486,NULL,'sports grounds'),(1487,NULL,'sports health'),(1488,NULL,'sports injuries and treatment'),(1489,NULL,'sports promotion and management services'),(1490,NULL,'sports shops'),(1491,NULL,'sports surfaces artificial'),(1492,NULL,'sports training and coaching'),(1493,NULL,'sportswear services'),(1494,NULL,'spraying equipment'),(1495,NULL,'spring making'),(1496,NULL,'squash courts'),(1497,NULL,'stables'),(1498,NULL,'stained glass'),(1499,NULL,'stainless steel products'),(1500,NULL,'stainless steel services'),(1501,NULL,'staircase materials and supplies'),(1502,NULL,'stairs lifts and escalators'),(1503,NULL,'stamp dealer services'),(1504,NULL,'stationary wholesalers'),(1505,NULL,'stationery'),(1506,NULL,'stationery manufacturers'),(1507,NULL,'steakhouses'),(1508,NULL,'steam cleaning equipment'),(1509,NULL,'steam cleaning services'),(1510,NULL,'steel building services'),(1511,NULL,'steel erectors'),(1512,NULL,'steel fabrications'),(1513,NULL,'steel processing'),(1514,NULL,'steel stock'),(1515,NULL,'steel stockholders'),(1516,NULL,'steel stockholders services'),(1517,NULL,'steeplejacks'),(1518,NULL,'stockbroker services'),(1519,NULL,'stockbrokers services'),(1520,NULL,'stocks and shares'),(1521,NULL,'stocktaking services'),(1522,NULL,'stone cleaners and restorers services'),(1523,NULL,'stone merchant services'),(1524,NULL,'stone products'),(1525,NULL,'stonemasons and drystone wallers services'),(1526,NULL,'storage'),(1527,NULL,'storage services'),(1528,NULL,'stove repairs'),(1529,NULL,'stoves and ovens'),(1530,NULL,'structural engineering services'),(1531,NULL,'structural engineers'),(1532,NULL,'structural engineers services'),(1533,NULL,'subjects and courses'),(1534,NULL,'sunbed equipment'),(1535,NULL,'sunglasses'),(1536,NULL,'sunroof services'),(1537,NULL,'supermarket services'),(1538,NULL,'supermarkets'),(1539,NULL,'surf boards'),(1540,NULL,'surplus stores'),(1541,NULL,'surveying equipment'),(1542,NULL,'surveyor and valuer services'),(1543,NULL,'suspended ceilings'),(1544,NULL,'swimming instruction services'),(1545,NULL,'swimming pool constructions'),(1546,NULL,'swimming pool dealers and installers'),(1547,NULL,'swimming pool equipment'),(1548,NULL,'t shirt printers'),(1549,NULL,'tableware'),(1550,NULL,'tai chi'),(1551,NULL,'tailoring services'),(1552,NULL,'tailors'),(1553,NULL,'tank and cistern makers'),(1554,NULL,'tank suppliers and installers'),(1555,NULL,'tanks'),(1556,NULL,'tape services'),(1557,NULL,'tapes adhesive and industrial'),(1558,NULL,'tar products'),(1559,NULL,'tarpaulin'),(1560,NULL,'tarpaulin covers'),(1561,NULL,'tattooists'),(1562,NULL,'tax consultants'),(1563,NULL,'taxi meters'),(1564,NULL,'taxidermists'),(1565,NULL,'taxis and private hire vehicles'),(1566,NULL,'tea importers and merchants'),(1567,NULL,'technical colleges'),(1568,NULL,'technical writing'),(1569,NULL,'telecommunication consultants'),(1570,NULL,'telecommunication equipment'),(1571,NULL,'telecommunication services'),(1572,NULL,'telegram and greetings services'),(1573,NULL,'telemarketing services'),(1574,NULL,'telephone answering and messaging services'),(1575,NULL,'telephone answering and messaging work'),(1576,NULL,'telephone equipment'),(1577,NULL,'telephone services'),(1578,NULL,'tennis and squash'),(1579,NULL,'tennis court makers'),(1580,NULL,'tennis court services'),(1581,NULL,'tenpin bowling centres'),(1582,NULL,'testing instruments and services'),(1583,NULL,'testing services and vehicles'),(1584,NULL,'textile machinery accessories'),(1585,NULL,'textile manufacturing'),(1586,NULL,'textile merchants services'),(1587,NULL,'textile printers equipment'),(1588,NULL,'textile services and supplies'),(1589,NULL,'thatching'),(1590,NULL,'thatching services'),(1591,NULL,'theatres and concert halls'),(1592,NULL,'theatrical agents'),(1593,NULL,'theatrical companies'),(1594,NULL,'theatrical costumes and accessories'),(1595,NULL,'theatrical services and supplies'),(1596,NULL,'theatrical special effects'),(1597,NULL,'theatrical supply services'),(1598,NULL,'therapy equipment'),(1599,NULL,'thermal engineers'),(1600,NULL,'thermal imaging'),(1601,NULL,'thermometers'),(1602,NULL,'ticket agencies'),(1603,NULL,'ties and scarves'),(1604,NULL,'tile materials'),(1605,NULL,'tile suppliers'),(1606,NULL,'tiling accessories'),(1607,NULL,'tiling services'),(1608,NULL,'timber frame buildings'),(1609,NULL,'timber frame buildings services'),(1610,NULL,'timber importers and agents'),(1611,NULL,'timber merchant services'),(1612,NULL,'timber merchants'),(1613,NULL,'time recorders and systems'),(1614,NULL,'timeshare'),(1615,NULL,'tobacco products'),(1616,NULL,'tobacconist services'),(1617,NULL,'tobacconists'),(1618,NULL,'tombstones'),(1619,NULL,'tool applications'),(1620,NULL,'tool hire'),(1621,NULL,'tool manufacturing services'),(1622,NULL,'tool suppliers and services'),(1623,NULL,'tools'),(1624,NULL,'tools and equipment hiring services'),(1625,NULL,'tourist attraction services'),(1626,NULL,'tourist attractions'),(1627,NULL,'tourist information'),(1628,NULL,'tours and sightseeing'),(1629,NULL,'tours and sightseeing services'),(1630,NULL,'tow bars'),(1631,NULL,'toy shops'),(1632,NULL,'toys and games'),(1633,NULL,'trade association services'),(1634,NULL,'trade associations'),(1635,NULL,'trade unions'),(1636,NULL,'traffic control and management'),(1637,NULL,'traffic control services'),(1638,NULL,'trailer hire'),(1639,NULL,'trailer parts'),(1640,NULL,'trailer suppliers'),(1641,NULL,'training'),(1642,NULL,'training methods and resources'),(1643,NULL,'training services'),(1644,NULL,'training services associations'),(1645,NULL,'translation services'),(1646,NULL,'translators and interpreters'),(1647,NULL,'transport consultant services'),(1648,NULL,'transport consultants'),(1649,NULL,'travel agents'),(1650,NULL,'tree care services'),(1651,NULL,'tree felling'),(1652,NULL,'trophies and medals'),(1653,NULL,'tropical fish'),(1654,NULL,'truck and trailer repairs'),(1655,NULL,'truck and trolley services'),(1656,NULL,'truck rebuilders'),(1657,NULL,'truck spares'),(1658,NULL,'truck trailer parts'),(1659,NULL,'truck trailers'),(1660,NULL,'tubes and fittings'),(1661,NULL,'tuition and courses'),(1662,NULL,'turbochargers'),(1663,NULL,'turf and soil supplies'),(1664,NULL,'turf preparation supplies'),(1665,NULL,'turfing services'),(1666,NULL,'turfing work'),(1667,NULL,'tutoring'),(1668,NULL,'tv and radio programmes'),(1669,NULL,'tv broadcasters'),(1670,NULL,'tv video and radio shops services'),(1671,NULL,'tvs dvd and video player hire'),(1672,NULL,'tvs dvd and video players repairs'),(1673,NULL,'tvs dvd and video players retail and suppliers'),(1674,NULL,'types of security services'),(1675,NULL,'typesetters'),(1676,NULL,'tyre dealer equipment'),(1677,NULL,'tyre repairs'),(1678,NULL,'tyre retreading'),(1679,NULL,'tyres'),(1680,NULL,'uniforms and staff wear'),(1681,NULL,'uninterruptible power supplies'),(1682,NULL,'universities'),(1683,NULL,'upholstered items'),(1684,NULL,'upholsterers'),(1685,NULL,'upholsterers supplies'),(1686,NULL,'used car dealers'),(1687,NULL,'vacuum cleaner systems'),(1688,NULL,'vacuum cleaners'),(1689,NULL,'valet services'),(1690,NULL,'valeting services'),(1691,NULL,'valuers'),(1692,NULL,'valves'),(1693,NULL,'vehicle accessories'),(1694,NULL,'vehicle builders'),(1695,NULL,'vehicle inspection'),(1696,NULL,'vehicle washing equipment and accessories'),(1697,NULL,'vending machines'),(1698,NULL,'ventilation services'),(1699,NULL,'ventilator and air vent equipment'),(1700,NULL,'vet supplies'),(1701,NULL,'vets'),(1702,NULL,'video and dvd hire'),(1703,NULL,'video and dvd services'),(1704,NULL,'video production'),(1705,NULL,'videoconferencing services'),(1706,NULL,'visitor attraction facilities'),(1707,NULL,'voice and data systems'),(1708,NULL,'wallpaper and paint services'),(1709,NULL,'wallpapers and paints'),(1710,NULL,'war games'),(1711,NULL,'warehouse services'),(1712,NULL,'warehouses'),(1713,NULL,'washing machine repairs'),(1714,NULL,'washing machine retail and suppliers'),(1715,NULL,'washroom services'),(1716,NULL,'waste disposal commercial'),(1717,NULL,'waste disposal equipment'),(1718,NULL,'waste oil services'),(1719,NULL,'waste processing machinery'),(1720,NULL,'waste services'),(1721,NULL,'water'),(1722,NULL,'water conservation'),(1723,NULL,'water coolers'),(1724,NULL,'water garden features'),(1725,NULL,'water management services'),(1726,NULL,'water softeners'),(1727,NULL,'water sports'),(1728,NULL,'water sports accessories'),(1729,NULL,'water supplier services'),(1730,NULL,'water suppliers'),(1731,NULL,'water transport'),(1732,NULL,'water treatment'),(1733,NULL,'water treatment equipment'),(1734,NULL,'water treatment services'),(1735,NULL,'wattle extract'),(1736,NULL,'weather forecasting equipment'),(1737,NULL,'weather services'),(1738,NULL,'web designers'),(1739,NULL,'webbing'),(1740,NULL,'website services'),(1741,NULL,'wedding accessories'),(1742,NULL,'wedding beauty treatments'),(1743,NULL,'wedding clothes hire'),(1744,NULL,'wedding directories'),(1745,NULL,'wedding entertainment'),(1746,NULL,'wedding food'),(1747,NULL,'wedding gift services'),(1748,NULL,'wedding gifts'),(1749,NULL,'wedding hire services'),(1750,NULL,'wedding honeymoons venues'),(1751,NULL,'wedding jewellery'),(1752,NULL,'wedding organisers'),(1753,NULL,'wedding stationery'),(1754,NULL,'wedding transportation'),(1755,NULL,'wedding venues'),(1756,NULL,'wedding videography'),(1757,NULL,'weddings cakes'),(1758,NULL,'weddings decorations'),(1759,NULL,'weddings general'),(1760,NULL,'weed control services'),(1761,NULL,'weighbridges'),(1762,NULL,'weight loss'),(1763,NULL,'welding and welders services'),(1764,NULL,'welding equipment'),(1765,NULL,'welding equipment services'),(1766,NULL,'welding methods and processes'),(1767,NULL,'welding tools'),(1768,NULL,'wheel services'),(1769,NULL,'wheels'),(1770,NULL,'wig and hairpiece products'),(1771,NULL,'will services'),(1772,NULL,'wills'),(1773,NULL,'wind turbines and windmills'),(1774,NULL,'window accessories'),(1775,NULL,'window cleaning'),(1776,NULL,'window cleaning equipment'),(1777,NULL,'window films'),(1778,NULL,'window repairs'),(1779,NULL,'window tinting'),(1780,NULL,'windows'),(1781,NULL,'windscreen work'),(1782,NULL,'windscreens'),(1783,NULL,'wine and spirit importer services'),(1784,NULL,'wine and spirit wholesale services'),(1785,NULL,'wine making supplies'),(1786,NULL,'wine producers and vineyards'),(1787,NULL,'wire products'),(1788,NULL,'wireless services'),(1789,NULL,'wood carvers services'),(1790,NULL,'wood preservation'),(1791,NULL,'wood preservation services'),(1792,NULL,'wood turned items'),(1793,NULL,'wood turning services'),(1794,NULL,'wooden windows'),(1795,NULL,'woodworking machinery'),(1796,NULL,'wool and knitting supplies'),(1797,NULL,'workwear'),(1798,NULL,'wrapping materials'),(1799,NULL,'writers'),(1800,NULL,'wrought ironwork'),(1801,NULL,'x-ray apparatus'),(1802,NULL,'x-ray services'),(1803,NULL,'xmas clubs'),(1804,NULL,'yacht brokers'),(1805,NULL,'yacht equipment'),(1806,NULL,'yachts'),(1807,NULL,'yoga'),(1808,NULL,'youth and community groups'),(1809,NULL,'zoos'),(1810,NULL,'pimpage'),(1811,NULL,'nonsense'),(1812,NULL,'pins'),(1813,NULL,'Nonsense'),(1814,NULL,'uncategorized'),(1815,NULL,'buttassniggers');
/*!40000 ALTER TABLE `businessCategories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessReviews`
--

DROP TABLE IF EXISTS `businessReviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businessReviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `justification` longtext,
  `score` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `reviewedId` bigint(20) NOT NULL,
  `reviewerId` bigint(20) NOT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3200943BBB9A287E` (`reviewerId`),
  KEY `FK3200943BD318A268` (`reviewedId`),
  CONSTRAINT `FK3200943BBB9A287E` FOREIGN KEY (`reviewerId`) REFERENCES `UserConnection` (`connection_id`),
  CONSTRAINT `FK3200943BD318A268` FOREIGN KEY (`reviewedId`) REFERENCES `businesses` (`business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessReviews`
--

LOCK TABLES `businessReviews` WRITE;
/*!40000 ALTER TABLE `businessReviews` DISABLE KEYS */;
INSERT INTO `businessReviews` VALUES (2,'Religion is terrible. They brainwash you, then they take all your money. It really depends on the religion how bad it is. Take some examples below:\n<br />\n<br />Catholicism: Priests molest young children\n<br />Hinduism: No beef\n<br />Islam: Bad in every respect\n<br />Iglesia ni Cristo: No Christmas\n<br />Buddhism: Moderate drinking only\n<br />Mormonism: Crazy people\n<br />Atheism: NOT a religion you crazy fucks',5,0,8,2,'2012-11-05 18:26:29'),(3,'Fuck SUMC it sucks ass',5,0,7,4,'2012-10-29 00:36:21'),(4,'I dunno... religion seems kind of OK\n<br />herp derp',3,0,8,6,'2012-10-29 00:36:21'),(5,'Religion is the best thing of all time',5,0,8,8,'2012-10-29 00:36:21'),(6,'Terrible. Let me make this abundantly clear. This is the best hospital within 100 miles. There is no other hospital close enough that will offer a tenth of the services available in this one. Recommended highly',1,0,7,8,'2012-10-29 02:15:55'),(7,'Grew up going to this place. It\'s as awesome as it\'s ever been',5,0,23,2,'2012-10-29 11:25:02'),(8,'meh',3,0,7,10,'2012-10-29 20:45:52'),(9,'Trololol? lolol?',4,0,25,2,'2012-11-05 18:23:07');
/*!40000 ALTER TABLE `businessReviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businessSubs`
--

DROP TABLE IF EXISTS `businessSubs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businessSubs` (
  `userId` bigint(20) NOT NULL,
  `businessId` bigint(20) DEFAULT NULL,
  KEY `FKD7E65E13FAC22CC4` (`userId`),
  CONSTRAINT `FKD7E65E13FAC22CC4` FOREIGN KEY (`userId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businessSubs`
--

LOCK TABLES `businessSubs` WRITE;
/*!40000 ALTER TABLE `businessSubs` DISABLE KEYS */;
INSERT INTO `businessSubs` VALUES (1,1),(3,7),(1,18),(1,17),(1,15),(1,7),(1,23);
/*!40000 ALTER TABLE `businessSubs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `businesses`
--

DROP TABLE IF EXISTS `businesses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `businesses` (
  `business_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `cellphone` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `domain` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullName` varchar(255) NOT NULL,
  `landline` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `coverpicId` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL,
  `profilepicId` bigint(20) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`business_id`),
  UNIQUE KEY `domain` (`domain`),
  KEY `FK6573DC6EB13377FB` (`coverpicId`),
  KEY `FK6573DC6EAF7A2949` (`profilepicId`),
  KEY `FK6573DC6E8FB898C5` (`owner_id`),
  KEY `FK6573DC6ECAA70DCA` (`categoryId`),
  CONSTRAINT `FK6573DC6E8FB898C5` FOREIGN KEY (`owner_id`) REFERENCES `UserConnection` (`connection_id`),
  CONSTRAINT `FK6573DC6EAF7A2949` FOREIGN KEY (`profilepicId`) REFERENCES `imgurs` (`imageId`),
  CONSTRAINT `FK6573DC6EB13377FB` FOREIGN KEY (`coverpicId`) REFERENCES `imgurs` (`imageId`),
  CONSTRAINT `FK6573DC6ECAA70DCA` FOREIGN KEY (`categoryId`) REFERENCES `businessCategories` (`categoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `businesses`
--

LOCK TABLES `businesses` WRITE;
/*!40000 ALTER TABLE `businesses` DISABLE KEYS */;
INSERT INTO `businesses` VALUES (1,'near foundation','09155411987','Internet games, notebooks, pens, school supplies and other such shit','baldwinternet','baldwinternet@gmail.com','Baldwinternet and Gaming','035 226 1489',0,0,3,1,7,1809),(3,'12345678901234567890123456789012345678901234567890123456789012345678901234567890','09155411987','1234567890123456789012345678901234567890123456789012345678901234567890','baldwinternet2','baldwinternet@gmail.com','123456789012345678901234567890123456789012345678901234567890','035 226 1489',0,0,NULL,1,NULL,1809),(4,'12345678901234567890123456789012345678901234567890123456789012345678901234567890','09155411987','1234567890123456789012345678901234567890123456789012345678901234567890','baldwinternet3','baldwinternet@gmail.com','1234567890123456789012345678901234567890','035 226 1489',0,0,NULL,1,11,1809),(6,'12345678901234567890123456789012345678901234567890123456789012345678901234567890','09155411987','123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890','baldwinternet4','baldwinternet@gmail.com','123456789012345678901234567890123456789012345','035 226 1489',0,0,NULL,1,NULL,1809),(7,'Perdices Street<br>Dumaguete City','09155411987','Got the drip, the herpes, or a bad drug addiction? Fear not! We at Silliman University Medical Center will cure your drug addled ass','sumc','baldwinternet@gmail.com','Silliman University Medical Center','035 226 1489',9.315969475026536,123.30418424606319,6,1,4,798),(8,'everywhere - in your heart','123','The Opiate of the Masses','religion','jesus@yourheart.com','Religion','123',0,0,9,3,10,1809),(9,'asfasdfa','asdfasdf','McDonald\'s Corporation (NYSE: MCD) is the world\'s largest chain of hamburger fast food restaurants, serving around 68 million customers daily in 119 c','longdescription','asdfsdaf','Long Description Business','adfasdf',0,0,NULL,1,NULL,1809),(10,'aaa','asdfasdf','asdfasdfs','pimphand','a@a.com','sadf','asdfasdf',0,0,NULL,1,309,1810),(11,'adfasfd','asdfasdf','adfasdfasf','aaaaaaa','asdfasdfasdfa@ass.com','asdasdf','adsfasdf',9.30687484059045,123.28832702636714,NULL,1,NULL,1811),(12,'','','','abcde','','abcdefg','',0,0,NULL,1,NULL,704),(13,'','','','qqqq5','','adsfasdfdsafasdf','',0,0,NULL,1,NULL,877),(14,'','','','qwer','','qwer','',0,0,NULL,1,NULL,4),(15,'Hangers @ Hypermart<br>Bagacay,<br>Dumaguete City','','','qwert','','qerr','',0,0,NULL,1,NULL,2),(16,'','','','baldwinterne','','aaaa','',0,0,311,1,310,1812),(17,'Katrina Homes 2,<br>6200 Dumaguete City,<br>Negros Oriental,<br>Philippines','','bad names are bad','badnames','','Bad names','',9.313333220024163,123.26493816375728,NULL,1,329,62),(18,'Near Foundation University front gate, Colon St., Dumaguete City','09155411987','Pins and shit we are peddling for the gullible people of Dumaguete City','pinsandshit','pinsandshit@gmail.com','Pins And Shit Dumaguete City','035 226 1489',9.305625500959993,123.30122308731075,NULL,1,328,1812),(19,'','','asdfasdfa','afasdfasdf','','asdfasdf','',9.300437517452728,123.28317718505855,NULL,1,NULL,1094),(20,'Everywhere','12345','Prostitutes and escorts for your carnal needs','prosti','lordmarkm@gmail.com','Prostitutes and Escorts Oh yeah','12345',9.308230051414103,123.29656677246089,NULL,1,NULL,1413),(21,'','','sadfasfasd','sdfasfasdf','','fasdfsadfasdf','',9.320426712314887,123.2588012695312,NULL,1,NULL,1814),(22,'nowhere<br>it is fake<br>12345','','business name says it all','fakesumc','','Fake Silliman University Medical Center','',9.32889636505482,123.29210357666011,NULL,3,NULL,798),(23,'Perdices Street corner Macias Street<br>Central Dumaguete City<br>Negros Oriental (6200)<br>Philippines ','09155411987','The number one supermarket in dumaguete city! Enjoy!','leeplaza','leeplaza@gmail.com','Lee Super Plaza','035 226 1489, 035 422 1092',0,0,NULL,3,360,1538),(24,'Perdices Street<br>Dumaguete City','09155411987','Supermarket that is always 2nd to Lee Super Plaza','cangs','cangs@cangs.co','Cangs Incorporated','035 226 1489, 035 422 1092',0,0,NULL,3,NULL,1538),(25,'Perdices Street<br>Near Holy Child Hospital<br>Dumaugete City','09155411987','A very old and very successful bakery and grocery in Dumaguete. Highly recommended','rickys','rickys@bakery.com','Rickys Bakery and Grocery','226 1489',9.308230051414103,123.29450683593745,359,3,358,1538),(26,'Ma. Cristina Street, Dumaguete City','','Conchita\'s is crap. Don\'t shop here','conchitas','conchitas@conchitas.com','Conchitas Super Extremely Supermark','',9.317843427326185,123.29416351318355,NULL,3,NULL,1538),(27,'Dumaguete City','','A subsidiary of lee plaza','hypermart','','Hypermart','',9.298404653955814,123.27150421142574,NULL,3,NULL,1538),(28,'Near Ceres Terminal and Don Bosco<br>Calindagan, Dumaguete City<br>Negros Oriental Philippines','09155411987','Pretty cool for a supermarket','robinsons','robinsons@supermarket.com','Robinsons Supermarket','035 226 1489, 035 422 1092, 036 133 2314',9.283835453462883,123.2862670898437,NULL,3,NULL,1538),(29,'Near Ceres Terminal<br>Colon Street<br>Dumaguete City','09194452312','A small wholesale store lol','chuyte','chuyte@gmail.com','Chuyte Mini Market','(035) 226-1489',9.301792753222133,123.31235961914058,NULL,1,NULL,1538);
/*!40000 ALTER TABLE `businesses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyandsell`
--

DROP TABLE IF EXISTS `buyandsell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buyandsell` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mode` int(11) DEFAULT NULL,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `imgurId` bigint(20) DEFAULT NULL,
  `ownerId` bigint(20) NOT NULL,
  `time` datetime DEFAULT NULL,
  `views` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2D7AC463D7ED73E8` (`imgurId`),
  KEY `FK2D7AC463EDB919AC` (`ownerId`),
  CONSTRAINT `FK2D7AC463D7ED73E8` FOREIGN KEY (`imgurId`) REFERENCES `imgurs` (`imageId`),
  CONSTRAINT `FK2D7AC463EDB919AC` FOREIGN KEY (`ownerId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyandsell`
--

LOCK TABLES `buyandsell` WRITE;
/*!40000 ALTER TABLE `buyandsell` DISABLE KEYS */;
INSERT INTO `buyandsell` VALUES (1,1,'Awesome don bosco pin','Don Bosco Pin',361,2,'2012-11-03 19:22:45',14),(2,2,'A beard for your neck negro','Neckbeard',362,2,'2012-11-03 19:22:45',12),(3,0,'big store','Lee Plaza',364,2,'2012-11-03 19:22:45',2),(4,0,'black phone','Phone',365,2,'2012-11-03 19:22:45',0),(5,0,'Own the number. People will need to pay you a royalty each time they use it.','The number 10',366,2,'2012-11-03 19:35:35',0),(6,0,'Own the color black','The color black',367,2,'2012-11-03 19:36:11',2),(7,1,'Cancer for sale','Cancer',369,2,'2012-11-03 23:57:11',55),(8,1,'Something','Everything',370,10,'2012-11-04 02:43:14',0),(9,1,'Something','Everything',371,10,'2012-11-04 02:43:14',9),(10,1,'You pick','Two random stars in the sky',372,2,'2012-11-04 02:48:58',1),(11,2,'something else','Something',373,10,'2012-11-04 02:50:11',2),(12,2,'You probably don\'t want this','Cancerous tumor',374,2,'2012-11-04 02:50:12',6),(13,2,'Eternal salvation of your soul','Salvation',375,4,'2012-11-05 15:17:15',0),(14,0,'The holy eucharist','Eucharist',376,4,'2012-11-05 15:18:46',6);
/*!40000 ALTER TABLE `buyandsell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyandsell_bidding`
--

DROP TABLE IF EXISTS `buyandsell_bidding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buyandsell_bidding` (
  `ends` date DEFAULT NULL,
  `increment` double DEFAULT NULL,
  `buyout` double DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `start` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK547D1B9F85F8C644` (`id`),
  CONSTRAINT `FK547D1B9F85F8C644` FOREIGN KEY (`id`) REFERENCES `buyandsell` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyandsell_bidding`
--

LOCK TABLES `buyandsell_bidding` WRITE;
/*!40000 ALTER TABLE `buyandsell_bidding` DISABLE KEYS */;
INSERT INTO `buyandsell_bidding` VALUES ('2012-11-06',NULL,60,1,15),('2012-11-21',NULL,456,7,123),('2012-11-07',NULL,10000,8,10000),('2012-11-07',NULL,10000,9,10000),('2012-11-10',NULL,15000,10,500);
/*!40000 ALTER TABLE `buyandsell_bidding` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyandsell_bids`
--

DROP TABLE IF EXISTS `buyandsell_bids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buyandsell_bids` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `itemId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK52F85D72BF2C3131` (`itemId`),
  KEY `FK52F85D72FAC22CC4` (`userId`),
  CONSTRAINT `FK52F85D72BF2C3131` FOREIGN KEY (`itemId`) REFERENCES `buyandsell_bidding` (`id`),
  CONSTRAINT `FK52F85D72FAC22CC4` FOREIGN KEY (`userId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyandsell_bids`
--

LOCK TABLES `buyandsell_bids` WRITE;
/*!40000 ALTER TABLE `buyandsell_bids` DISABLE KEYS */;
INSERT INTO `buyandsell_bids` VALUES (1,20,'2012-11-04 22:19:37',2,7),(2,50,'2012-11-04 22:28:55',2,7),(3,51,'2012-11-04 22:44:21',4,7),(4,55,'2012-11-05 09:49:01',4,7),(5,67.3,'2012-11-05 10:23:46',4,7),(6,80,'2012-11-05 10:44:35',4,7),(7,95,'2012-11-05 10:46:56',4,7),(8,110,'2012-11-05 10:47:11',4,7),(9,123,'2012-11-05 10:47:52',4,7),(10,140,'2012-11-05 10:48:08',4,7),(11,15,'2012-11-05 11:11:42',4,1),(12,152.3,'2012-11-05 11:14:04',4,7),(13,165,'2012-11-05 11:15:11',4,7),(14,177.5,'2012-11-05 14:49:33',4,7),(15,190,'2012-11-05 14:54:02',4,7),(16,17,'2012-11-05 14:55:32',4,1);
/*!40000 ALTER TABLE `buyandsell_bids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyandsell_fixedprice`
--

DROP TABLE IF EXISTS `buyandsell_fixedprice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buyandsell_fixedprice` (
  `negotiable` tinyint(1) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK262F57B185F8C644` (`id`),
  CONSTRAINT `FK262F57B185F8C644` FOREIGN KEY (`id`) REFERENCES `buyandsell` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyandsell_fixedprice`
--

LOCK TABLES `buyandsell_fixedprice` WRITE;
/*!40000 ALTER TABLE `buyandsell_fixedprice` DISABLE KEYS */;
INSERT INTO `buyandsell_fixedprice` VALUES (1,1000,3),(1,20,4),(0,500,5),(1,5000,6),(1,5000,14);
/*!40000 ALTER TABLE `buyandsell_fixedprice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buyandsell_trade`
--

DROP TABLE IF EXISTS `buyandsell_trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buyandsell_trade` (
  `tradefor` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD15028885F8C644` (`id`),
  CONSTRAINT `FKD15028885F8C644` FOREIGN KEY (`id`) REFERENCES `buyandsell` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buyandsell_trade`
--

LOCK TABLES `buyandsell_trade` WRITE;
/*!40000 ALTER TABLE `buyandsell_trade` DISABLE KEYS */;
INSERT INTO `buyandsell_trade` VALUES ('Yellow cap',2),('Your Soul',11),('Your soul',12),('Your prayers',13);
/*!40000 ALTER TABLE `buyandsell_trade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `categoryId` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `businessId` bigint(20) NOT NULL,
  `mainpicId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`categoryId`),
  KEY `FK4D47461CA98985F1` (`businessId`),
  KEY `FK4D47461C5790B739` (`mainpicId`),
  CONSTRAINT `FK4D47461C5790B739` FOREIGN KEY (`mainpicId`) REFERENCES `imgurs` (`imageId`),
  CONSTRAINT `FK4D47461CA98985F1` FOREIGN KEY (`businessId`) REFERENCES `businesses` (`business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Got The Blob growing inside your colon? What about your man-boob (male breast cancer is more common than you may think)? Detect that shit now so we can nuke it and your immune system straight to hell! SUMC Cancer Detection Services - the prelude to the apocalypse.','SUMC Cancer Detection Services ',7,18),(2,'Excited about death? Do memorial services turn you on? Don\'t wait until you\'re dead! You can have your memorial service today. Enjoy the sight of crying relatives from the comfort of your casket. SUMC Pre-death Burial services: A Grim look into your certain doom.','Pre-death Burial services',7,NULL),(3,'We just stopped caring. One day, we realized \"Holy shit, we really just dont give 2 shits\" and that was that, you know? I just want this description to be moderately long. I want at most 3.5 lines on the description section of the homepage. I think that is the optimal length.','Phoned-in category',7,22),(4,'And yet we still make these things. Why?','Phoned-in categor 2',7,17),(5,'Internet-related services such as chat, webcam, and porno','Internet services',1,16),(6,'The Big One. You don\'t want to mess with Catholicism. Because blah. blah. blah','Catholicism',8,NULL),(7,'nigger','butt butt',1,NULL),(8,'nigger','butt butt',8,NULL),(9,'what','ass ass',1,38),(10,'Catholic fan fiction','Mormonism',8,NULL),(11,'stuff','stuff',6,NULL),(12,'for tourists','Tourism pins',16,NULL),(13,'','Fake cancers',22,NULL),(14,'Used in Buglasan','Tourism Pins',18,344);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoryPics`
--

DROP TABLE IF EXISTS `categoryPics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoryPics` (
  `imageId` bigint(20) NOT NULL,
  `categoryId` bigint(20) NOT NULL,
  PRIMARY KEY (`imageId`,`categoryId`),
  UNIQUE KEY `categoryId` (`categoryId`),
  KEY `FK19660247B2E40FC6` (`categoryId`),
  KEY `FK19660247423EF607` (`imageId`),
  CONSTRAINT `FK19660247423EF607` FOREIGN KEY (`imageId`) REFERENCES `categories` (`categoryId`),
  CONSTRAINT `FK19660247B2E40FC6` FOREIGN KEY (`categoryId`) REFERENCES `imgurs` (`imageId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoryPics`
--

LOCK TABLES `categoryPics` WRITE;
/*!40000 ALTER TABLE `categoryPics` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoryPics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imgurs`
--

DROP TABLE IF EXISTS `imgurs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `imgurs` (
  `imageId` bigint(20) NOT NULL AUTO_INCREMENT,
  `deletehash` varchar(255) DEFAULT NULL,
  `hash` varchar(255) NOT NULL,
  `uploaded` datetime DEFAULT NULL,
  `description` varchar(140) DEFAULT NULL,
  `hidden` tinyint(1) DEFAULT NULL,
  `title` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`imageId`)
) ENGINE=InnoDB AUTO_INCREMENT=379 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imgurs`
--

LOCK TABLES `imgurs` WRITE;
/*!40000 ALTER TABLE `imgurs` DISABLE KEYS */;
INSERT INTO `imgurs` VALUES (3,'XKhwKEg8IuPnjru','RfpU1','2012-10-10 16:00:48',NULL,0,NULL),(4,'nwHOQemzGAmF6Yo','09Mth','2012-10-10 16:16:52',NULL,0,NULL),(6,'bpgG41FCUj8iNNm','43D23','2012-10-10 16:17:52',NULL,0,NULL),(7,'FMvTvhWael4PfHD','ehNBE','2012-10-10 16:53:04',NULL,0,NULL),(9,'jyzPZ2LeGNlZhvX','xLCoS','2012-10-10 18:28:45',NULL,0,NULL),(10,'cNB8Pfbxh6XsdV7','z5vpn','2012-10-10 18:30:05',NULL,0,NULL),(11,'27VBlEfPzLedV6m','wjtBY','2012-10-13 03:47:43',NULL,0,NULL),(12,'Atb5ETG9Ep3fe1w','AJuh9',NULL,NULL,0,NULL),(13,'SZ8qKewkpebQzlr','e86Ad',NULL,NULL,0,NULL),(14,'iWyfs3LVZQIpUIs','kyFBN',NULL,NULL,0,NULL),(15,'noYfwruEDhn4dAJ','CBTwe',NULL,NULL,0,NULL),(16,'D4drsQWq7S20nVT','l7djs',NULL,NULL,0,NULL),(17,'FkrQwg7AsiIqRBi','OUlsZ',NULL,NULL,0,NULL),(18,'WsGpWti7sti3B0z','Nl36w',NULL,NULL,0,NULL),(19,'78ejjJxGLjGAdey','ufM6A',NULL,NULL,0,NULL),(20,'4VgfCjBRZrS8DTO','RziH7',NULL,NULL,0,NULL),(21,'BYWTdWVe945ta0i','altvl',NULL,NULL,0,NULL),(22,'lGgEvEGs7oGAMNP','6HO6i',NULL,NULL,0,NULL),(23,'BdoTb4V2MfK8PGJ','pzLab',NULL,NULL,0,NULL),(24,'ZvpcWqt0n3yHvJz','kPrmv',NULL,NULL,0,NULL),(25,'VxukwSEscz7oFvk','f3Kov',NULL,NULL,0,NULL),(26,'csmDRwrHfIrJJNf','xTGMl',NULL,NULL,0,NULL),(27,'hf9Lmbs4BsaLYzd','OFxn0',NULL,NULL,0,NULL),(28,'XEXdqpFdEoGygqY','rQTzK',NULL,NULL,0,NULL),(29,'NEXL2vk5NUHCRno','4ejeF',NULL,NULL,0,NULL),(30,'FvdypFFbuoddew5','99etj',NULL,NULL,0,NULL),(31,'pF0z28QlRYdNS6a','Z6bRG',NULL,NULL,0,NULL),(32,'drrhbO992TR4Bgx','D5qjc',NULL,NULL,0,NULL),(33,'YWWBOu1xo1bl3KB','a6ufl',NULL,NULL,0,NULL),(34,'xWCAzJ4w3C1KArQ','ZfCks',NULL,NULL,0,NULL),(35,'ab1xoRmjc7B77cD','Ka3sP',NULL,NULL,0,NULL),(36,'lgciDtM9VCMj8C4','QcJqm',NULL,NULL,0,NULL),(37,'V5rY11TsxClxumq','jNeHi',NULL,NULL,0,NULL),(38,'8httw1INF3tcv8b','rbmy4',NULL,NULL,0,NULL),(39,'rHBrzgDo4Ra8RPf','QDyt3',NULL,NULL,0,NULL),(40,'0g7AqzeAWEp4v6L','Sftyv',NULL,NULL,0,NULL),(41,'0g7AqzeAWEp4v6L','Sftyv',NULL,NULL,0,NULL),(42,'NO5Ja8l4VKO9Lgh','7P76j','2012-10-14 00:01:44',NULL,0,NULL),(43,'2TgBCB2BYjgDXil','S53fI','2012-10-14 00:56:13',NULL,0,NULL),(44,'2TgBCB2BYjgDXil','S53fI','2012-10-14 00:56:21',NULL,0,NULL),(45,'BdFSqZeDON1Dsxx','rXPM4','2012-10-14 00:56:48',NULL,0,NULL),(46,'DFFs3DdTsxqcTsh','NK01n','2012-10-14 00:57:36',NULL,0,NULL),(47,'BCTeuGcmNZkaz17','GJxVQ','2012-10-14 01:51:54',NULL,0,NULL),(48,'mXwJ6bAOEib2HT3','CPFBg','2012-10-14 01:52:13',NULL,0,NULL),(49,'HxrbhVWdIaNMb9a','blOUc','2012-10-14 15:22:12',NULL,0,NULL),(50,'K47XOPb3p6sNzoe','a3Ok8','2012-10-14 17:38:13',NULL,0,NULL),(51,'y40uGbYhqwYoWl2','nbGqP','2012-10-14 21:45:20',NULL,0,NULL),(52,'1okkslekiyVykPI','8y2ey','2012-10-15 13:07:41',NULL,0,NULL),(53,'hOlv5nxJPkvLAyL','3Cay8','2012-10-15 13:07:56',NULL,0,NULL),(54,'TIR2aQGBq05LS64','sfrOd','2012-10-15 13:07:57',NULL,0,NULL),(55,'WRQkpHuheFNGeHz','tAY70','2012-10-15 13:08:59',NULL,0,NULL),(56,'3tCHIwQZjhJ0ANu','kwMr0','2012-10-15 13:08:59',NULL,0,NULL),(57,'Q6PDkSqBatCbQSg','tHDcC','2012-10-15 13:09:00',NULL,0,NULL),(58,'labCw00EhnLrtFR','3mRRr','2012-10-15 13:09:00',NULL,0,NULL),(59,'4Kuugo5zHLkgztG','SQcCf','2012-10-15 13:09:01',NULL,0,NULL),(60,'gTQ4MKZAYTJH7De','08k3h','2012-10-15 13:09:01',NULL,0,NULL),(61,'5PWMSruVJzDgGol','eBF1I','2012-10-15 13:16:13',NULL,0,NULL),(62,'K4Gqv6HOZRhLUQs','DAbCA','2012-10-15 13:16:14',NULL,0,NULL),(63,'JfbAH0OkMdQoIJp','mFMXI','2012-10-15 13:16:14',NULL,0,NULL),(64,'tITuhv5Atnck3LN','wFscZ','2012-10-15 13:16:14',NULL,0,NULL),(65,'lgFVspTDZO99w9a','LESuX','2012-10-15 13:16:15',NULL,0,NULL),(66,'CgZSb3gPWFY2wij','0hErl','2012-10-15 13:16:17',NULL,0,NULL),(67,'OeeejsYO4glRj9n','pJfK0','2012-10-15 13:16:54',NULL,0,NULL),(68,'AtgmmqKpjy3CKST','jzCpl','2012-10-15 13:16:54',NULL,0,NULL),(69,'D266o5H33B5XFmt','KszEZ','2012-10-15 13:16:54',NULL,0,NULL),(70,'Dq3FIRTiRO0dUNf','6t0Zb','2012-10-15 13:16:55',NULL,0,NULL),(71,'nU3mkIWCRl2hiEV','EvjR0','2012-10-15 13:16:55',NULL,0,NULL),(72,'G8oSdDyQL2Bsz5d','diPPk','2012-10-15 13:16:55',NULL,0,NULL),(73,'0DEiQqLHeYvQw6M','VrSbu','2012-10-15 13:26:36',NULL,0,NULL),(74,'0lZXY8TAPktSgIT','KU0zT','2012-10-15 13:26:51',NULL,0,NULL),(75,'waDeOl1ZsXjdFMw','IdxLg','2012-10-15 13:26:52',NULL,0,NULL),(76,'E8tEahSTzX8ANWV','9XHVI','2012-10-15 13:26:53',NULL,0,NULL),(77,'rkkobNNzXlhqgCO','e0QzK','2012-10-15 13:26:54',NULL,0,NULL),(78,'TjAE9kF5cMRRIzg','Y2jwi','2012-10-15 13:26:54',NULL,0,NULL),(79,'tvRmkf76zeHc0g3','1vrtV','2012-10-15 13:26:55',NULL,0,NULL),(80,'Tje5vosmGjn7szZ','I8nM8','2012-10-15 13:30:50',NULL,0,NULL),(81,'bfnSo7LJMTBMQBU','ara7u','2012-10-15 13:31:04',NULL,0,NULL),(83,'FwE971D0KVti8MS','YUFCX','2012-10-15 13:31:04',NULL,0,NULL),(84,'YKy8ZfN177wc11G','cRX5C','2012-10-15 13:31:04',NULL,0,NULL),(85,'SHW6N6cC9op7KfJ','FiQ53','2012-10-15 13:31:04',NULL,0,NULL),(87,'ZMRnnobEjxn3VzB','tPMKB','2012-10-15 13:41:34',NULL,0,NULL),(88,'aNiDzgXO5GiBYPl','HfjHi','2012-10-15 13:41:34',NULL,0,NULL),(89,'E1exviGx3Ysfwk7','ULHmb','2012-10-15 13:41:34',NULL,0,NULL),(90,'TCuU5PRZW8kzpwi','7JzxS','2012-10-15 13:41:36',NULL,0,NULL),(91,'OYPJv3s3sB1QNcc','M9GJt','2012-10-15 13:41:36',NULL,0,NULL),(92,'twIzy4zL4Ttmx0b','TevKV','2012-10-15 13:41:38',NULL,0,NULL),(93,'e2WmDMkIxox60uo','VIPKL','2012-10-15 13:45:59',NULL,0,NULL),(94,'xLmFZi9eb5YdEM0','Gu36W','2012-10-15 13:45:59',NULL,0,NULL),(95,'CxPZg87vYmxe7Bs','cdnO5','2012-10-15 13:46:01',NULL,0,NULL),(96,'4bNmA5o6nEVvqdc','uDr2Q','2012-10-15 13:46:02',NULL,0,NULL),(97,'w2GC1cUuvCN2x8M','tU1ie','2012-10-15 13:46:02',NULL,0,NULL),(98,'4YXRJphuTbur0Zy','wealH','2012-10-15 13:46:03',NULL,0,NULL),(99,'ImOHKWVJ6IEsGVq','elVbP','2012-10-15 13:48:15',NULL,0,NULL),(100,'LLfcl1C4ro4iUmW','fQkQI','2012-10-15 13:48:47',NULL,0,NULL),(101,'ag1aYCSEDcmNMoO','YeeWW','2012-10-15 13:48:48',NULL,0,NULL),(102,'acSQMA3oSX6LIDI','L65mz','2012-10-15 13:48:48',NULL,0,NULL),(103,'Oufw9G4bJLwQZpL','cTI6R','2012-10-15 13:48:49',NULL,0,NULL),(104,'mEHlho4nEkQI93H','hbkCx','2012-10-15 13:48:49',NULL,0,NULL),(122,'6TR31bmucE76ApX','It4id','2012-10-15 14:21:46',NULL,0,NULL),(123,'CZgci1UePH5wXa7','JFdaf','2012-10-15 14:22:29',NULL,0,NULL),(124,'yaIXZkzfC2ppDPs','jDLdZ','2012-10-15 14:22:30',NULL,0,NULL),(125,'rl5wMg2kDQrUOLr','7EmdB','2012-10-15 14:22:32',NULL,0,NULL),(126,'ZLp1W5LPXXGq72U','NM7n9','2012-10-15 14:23:39',NULL,0,NULL),(127,'wK9xI4izcFEHx3w','cbN2g','2012-10-15 14:24:33',NULL,0,NULL),(128,'ngfc198qupIRPrL','qzVq8','2012-10-15 14:38:00',NULL,0,NULL),(132,'X8jPNWvPZLJ1IVD','B2LVD','2012-10-15 14:41:07',NULL,0,NULL),(142,'iJq5cbaElx0lkdb','i3W7A','2012-10-15 15:10:24','x<br>x<br>x<br>x<br>x<br>x',0,'x'),(143,'oD9Dhzu8uRiuLrM','xJHKH','2012-10-15 15:11:28','adfasdf',0,'wad'),(144,'Ym2UzntAYB8fhBD','jRPpA','2012-10-15 15:11:29','Colon cancer - 2 days manifestation time. The pain at this point is excruciating',0,'Colon Cancer 2 days'),(145,'BjGgq8BOsnVI0gJ','yyhDS','2012-10-15 15:15:01',NULL,0,NULL),(182,'SEbcM6AVWdHKrKK','k0Llq','2012-10-17 15:19:04',NULL,0,NULL),(213,'tbCYHno6xEwDPBY','tfLTY','2012-10-17 16:36:59',NULL,0,NULL),(221,'PNP2Ywyff772Fkw','BV3Ou','2012-10-17 16:37:08',NULL,0,NULL),(274,'VRy7OxGUPRHoXga','7KiwI','2012-10-17 16:59:33',NULL,0,NULL),(275,'bUeUoDNKVMfDvbb','NoHmh','2012-10-17 16:59:33',NULL,0,NULL),(276,'S4Wxr2MTf5iX6sw','8Qo0V','2012-10-17 16:59:33',NULL,0,NULL),(278,'TfuInnnZbkv0XiQ','FjchZ','2012-10-17 17:02:03',NULL,0,NULL),(304,'d5d2vFrEPhJqQDX','49gWN','2012-10-17 18:40:19',NULL,0,NULL),(306,'QTJnkPasQOcjT2V','bWo46','2012-10-17 18:40:19',NULL,0,NULL),(307,'FOfl7UNV0J4IXlL','Hc4xW','2012-10-18 13:06:47','Enjoy your beard, motherfucker',0,'Neckbeard'),(308,'laaXNzwfG447zxv','VEHBI','2012-10-18 17:09:36',NULL,0,NULL),(309,'caxa3btQtlD0us8','SzzCz','2012-10-19 13:57:01',NULL,0,NULL),(310,'16RQFDshkyfzoDR','pj66s','2012-10-19 20:39:42',NULL,0,NULL),(311,'H0bqcCjE6RQdAB6','z3HsR','2012-10-19 20:40:06',NULL,0,NULL),(312,'q2IFL44hpEbF3X6','w8Dts','2012-10-20 00:06:38',NULL,0,NULL),(313,'1OueP8tpA4MUGJ4','NUYsX','2012-10-20 00:06:43',NULL,0,NULL),(314,'9n8SQgKRpXTf6zT','bf9y9','2012-10-20 00:06:59',NULL,0,NULL),(315,'N0PfyBBC5UlMh61','YqKqb','2012-10-20 00:07:03',NULL,0,NULL),(316,'sJ2WmjWL7e7Fvt6','maPaF','2012-10-20 00:07:09',NULL,0,NULL),(317,'6ijPMyLBpzYzste','DoQBh','2012-10-20 00:07:13',NULL,0,NULL),(318,'2OkvbXz9HdrTa4M','X1Z4N','2012-10-20 00:12:11',NULL,0,NULL),(319,'CcT9HTm7xNLc0cc','uzvKA','2012-10-20 00:13:24',NULL,0,NULL),(320,'rDvcHyValQRrbOa','fAQbY','2012-10-20 00:13:34',NULL,0,NULL),(321,'Bxbu5rtVlgLDt3P','NGhXd','2012-10-20 00:13:34',NULL,0,NULL),(322,'izZaspfIqaWjGwE','A4jZA','2012-10-20 00:13:35','circle',0,'lingin'),(323,'jRfuIW3FYpilC5e','fInRV','2012-10-20 00:13:35',NULL,0,NULL),(324,'iLVEogS4I5V1F4b','Nkfl5','2012-10-20 00:13:35',NULL,0,NULL),(325,'7qzmJTZLwv9oOWp','xzzos','2012-10-20 00:13:35',NULL,0,NULL),(326,'ruIzsBfnPuOpzJA','EKuiM','2012-10-20 00:13:36',NULL,0,NULL),(327,'1ql848gG9QLJlWs','HUdLn','2012-10-20 00:17:39',NULL,0,NULL),(328,'DpZk3feDmaUUR0n','gVBWJ','2012-10-21 22:58:15',NULL,0,NULL),(329,'E9kVK0fL19xtcaB','FLRVz','2012-10-22 13:39:13',NULL,0,NULL),(330,'FrunPweeqf9vvXZ','qXeOX','2012-10-22 13:49:02',NULL,0,NULL),(331,'isIb1GwblGlfvi4','IF00z','2012-10-22 13:49:02',NULL,0,NULL),(332,'rMOfTGJxL6i5mOf','h9e3M','2012-10-22 13:49:02',NULL,0,NULL),(333,'cWYjhCGSPdDxR9w','zMhsE','2012-10-22 13:49:02',NULL,0,NULL),(334,'zp2UJv7IEI23gkY','XnlIq','2012-10-22 13:49:02',NULL,0,NULL),(335,'IZ2BxnNtd0AP3QF','j2Nz8','2012-10-22 13:49:02',NULL,0,NULL),(336,'woEdUlponhspQHy','gYAWq','2012-10-22 13:49:09',NULL,0,NULL),(337,'cMunoDgva4hYMEm','LqVkB','2012-10-24 17:35:34',NULL,0,NULL),(338,'utHRMUewfyyyUkq','UgWYv','2012-10-24 17:35:59',NULL,0,NULL),(339,'gRLxwlQVMxKrPzk','O9pD4','2012-10-24 18:00:35',NULL,0,NULL),(340,'ymawHyou5WTcIn5','Mig4K','2012-10-24 18:01:07',NULL,0,NULL),(341,'iEwW8p9FhufKedW','I40mR','2012-10-24 18:01:41',NULL,0,NULL),(342,'LIs89ybKsSoDgkg','ZoOHy','2012-10-24 18:05:50',NULL,0,NULL),(343,'htVPDe4Q1uIt0GF','sWWPT','2012-10-24 18:07:30',NULL,0,NULL),(344,'G9VjFLn4bsXQrUs','TVq2b',NULL,NULL,0,NULL),(345,'ukcIs9ceHUg732S','uUydU','2012-10-24 18:23:05',NULL,0,NULL),(346,'440GUy7xRZnOJ5M','5Dzih','2012-10-24 18:23:44','Based on the popular new york ad campaign',0,'I heard Dumaguete'),(347,'qlyxp0is61oquCM','av0zY','2012-10-24 18:25:37',NULL,0,NULL),(348,'I2tQyu4TfqqPMfY','EAgmQ','2012-10-24 18:25:37',NULL,0,NULL),(349,'WQANpZIKrC7XybV','aPoul','2012-10-24 18:25:37',NULL,0,NULL),(350,'7LXNOqzILupHaLW','Pv4wb','2012-10-24 18:25:37',NULL,0,NULL),(351,'W7jhV36tTX4jV4Y','3elHU','2012-10-24 18:25:38',NULL,0,NULL),(352,'DdaQruCrxdK9xFi','2Wk24','2012-10-24 18:25:39',NULL,0,NULL),(353,'UK0DA57wgUvvo4k','wPNCM','2012-10-24 18:25:40',NULL,0,NULL),(354,'mvBt1SffqnV4D8C','0Ws2a','2012-10-24 18:25:46',NULL,0,NULL),(355,'VScDel5ShRf0jwJ','WXN2f','2012-10-24 18:26:00',NULL,0,NULL),(356,'0OyD0YGfqRzyJWN','Roo5U','2012-10-25 14:23:58',NULL,0,NULL),(357,'IC2eotMXDEcGKkN','ZtFPa','2012-10-25 22:32:45',NULL,0,NULL),(358,'mh9RKAmDKijcDYR','o7fLv','2012-10-27 21:33:58',NULL,0,NULL),(359,'B72Y1DxdkdHpoyt','TGEtR','2012-10-27 21:35:01',NULL,0,NULL),(360,'0LLoH30OI4OjrS3','hI69W','2012-10-29 11:22:54',NULL,0,NULL),(361,'v5lUXDEYkyipvwe','jVOGT',NULL,NULL,0,NULL),(362,'DwEgHLXg3H3giLZ','JEvQe',NULL,NULL,0,NULL),(364,'jMmWaHhJPkbnkae','mFW96',NULL,NULL,0,NULL),(365,'eTYnc1F4962g5Q7','XWYhZ',NULL,NULL,0,NULL),(366,'Jh2fbwiYcN32m8o','Mhkad',NULL,NULL,0,NULL),(367,'yBVifu0cudwdBgC','M1psH',NULL,NULL,0,NULL),(369,'ejVAwDiPaCF49Hs','1zPPh',NULL,NULL,0,NULL),(370,'H8ZerlMcv6MuyHl','HnSME',NULL,NULL,0,NULL),(371,'H8ZerlMcv6MuyHl','HnSME',NULL,NULL,0,NULL),(372,'0D31OecqOPBIUkM','OOneM',NULL,NULL,0,NULL),(373,'IPsQF1meJsJr02F','I1UK6',NULL,NULL,0,NULL),(374,'TnMNqRfsrHSfiOM','yfASf',NULL,NULL,0,NULL),(375,'NkmnC4qlH1wkxHB','QwX7Q',NULL,NULL,0,NULL),(376,'WtQuG3vyvgi68gZ','471a7',NULL,NULL,0,NULL),(377,'o9YugRRrMTXMhHs','LDky0','2012-11-05 17:45:02',NULL,0,NULL),(378,'XYFywygWMpHkEco','bXTEo','2012-11-05 17:45:06',NULL,0,NULL);
/*!40000 ALTER TABLE `imgurs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `postId` bigint(20) NOT NULL AUTO_INCREMENT,
  `postTime` datetime DEFAULT NULL,
  `posterId` bigint(20) DEFAULT NULL,
  `text` longtext,
  `title` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `attachmentId` bigint(20) DEFAULT NULL,
  `attachmentTitle` varchar(255) DEFAULT NULL,
  `attachmentType` int(11) DEFAULT NULL,
  `posterTitle` varchar(255) DEFAULT NULL,
  `attachmentImgurHash` varchar(255) DEFAULT NULL,
  `posterImgurHash` varchar(255) DEFAULT NULL,
  `attachmentIdentifier` varchar(500) DEFAULT NULL,
  `posterIdentifier` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`postId`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (1,'2012-10-11 17:47:45',7,'hue<br>hue<br>hue','&lt;script&gt;alert(\'Hack!\');&lt;/script&gt;',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(3,'2012-10-11 18:05:43',7,'Got the drip, the herpes, or a bad drug addiction? Fear not! We at Silliman University Medical Center will cure your drug addled ass<br>www.indumaguete.com/p/sumc<br>Fake owner: Mark Martinez<br>','Silliman Medical Center now open for business! 10 year promo!',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(4,'2012-10-11 18:44:22',7,'New post!<br>Trololol','New post!',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(5,'2012-10-11 18:44:40',7,'Just to see the fade-in effect','I like to post',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(6,'2012-10-11 18:44:49',7,'','Let\'s see it again!',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(7,'2012-10-11 18:47:06',1,'Come see how miserable our shop is!','Baldwinternet and Gaming now open!',1,NULL,NULL,NULL,'Baldwinternet and Gaming',NULL,'ehNBE',NULL,'baldwinternet'),(8,'2012-10-11 18:49:52',1,'yep ok','ass',1,NULL,NULL,NULL,'Baldwinternet and Gaming',NULL,'ehNBE',NULL,'baldwinternet'),(9,'2012-10-11 18:50:04',6,'numbers','we seel',1,NULL,NULL,NULL,'Some other business',NULL,NULL,NULL,NULL),(10,'2012-10-11 19:02:14',7,'bcd','a',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(12,'2012-10-12 14:12:32',1,'This post will get some money deducted from its salary for being late!','My Latest Post!',1,NULL,NULL,NULL,'Baldwinternet and Gaming',NULL,'ehNBE',NULL,'baldwinternet'),(13,'2012-10-14 21:45:51',7,'I hope you all die','Fuck you demanding patients',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(14,'2012-10-15 17:55:19',1,'The one with the vader theme','This post made with the new box',1,NULL,NULL,NULL,'Baldwinternet and Gaming',NULL,'ehNBE',NULL,'baldwinternet'),(15,'2012-10-18 21:06:39',7,'','',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(16,'2012-10-18 21:06:46',7,'wtf','wtf',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(17,'2012-10-19 17:56:28',1,'It still looks like complete crap','I can\'t believe I\'ve been at this for 8 days now',1,NULL,NULL,NULL,'Baldwinternet and Gaming',NULL,'ehNBE',NULL,'baldwinternet'),(18,'2012-10-19 20:40:36',16,'adto mo homies','Hi Grand opening namo ugma',1,NULL,NULL,NULL,'Some other business',NULL,NULL,NULL,NULL),(19,'2012-10-21 01:08:14',1,'won\'t work','new post system',1,NULL,NULL,NULL,'Baldwinternet and Gaming',NULL,'ehNBE',NULL,'baldwinternet'),(20,'2012-10-21 22:17:32',17,'That\'s right','w00t first business with a latLng bitches!',1,NULL,NULL,NULL,'Nigger Butt Asses',NULL,NULL,NULL,'assbuttnigger'),(21,'2012-10-21 22:58:03',18,'You god damned motherfuckers','Buy some pins',1,NULL,NULL,NULL,'Pins And Shit Dumaguete City',NULL,NULL,NULL,'pinsandshit'),(22,'2012-10-21 22:59:35',7,'The Jury must Acquit','If the Economy is fit',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(23,'2012-10-22 13:38:57',17,'Business is pretty bad','Yeah well',1,NULL,NULL,NULL,'Nigger Butt Asses',NULL,NULL,NULL,'assbuttnigger'),(24,'2012-10-22 13:39:33',17,'This picture of a priest will help boost sales...','Maybe',1,NULL,NULL,NULL,'Nigger Butt Asses',NULL,'FLRVz',NULL,'assbuttnigger'),(25,'2012-10-23 17:16:58',2,'World, hello :)','Hello world',0,NULL,NULL,1,'Mark Martinez','IHOEQ','http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(26,'2012-10-23 17:18:21',2,'World, hello :)','Hello world',0,NULL,NULL,1,'Mark Martinez','oftqx','http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(27,'2012-10-23 17:20:55',7,'World, hello :)','Hello world',1,NULL,NULL,1,'Silliman University Medical Center','EHHnn','09Mth',NULL,'sumc'),(28,'2012-10-23 17:47:17',2,'1. There is a God<br>2. There is hope','If this works, then',0,NULL,NULL,2,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture','<iframe width=\"640\" height=\"480\" src=\"http://www.youtube.com/embed/y8Kyi0WNg40?rel=0\" frameborder=\"0\" allowfullscreen></iframe>','Mark Martinez'),(29,'2012-10-23 17:47:34',7,'1. There is a God<br>2. There is hope','If this works, then',1,NULL,NULL,2,'Silliman University Medical Center',NULL,'09Mth','<iframe width=\"640\" height=\"480\" src=\"http://www.youtube.com/embed/y8Kyi0WNg40?rel=0\" frameborder=\"0\" allowfullscreen></iframe>','sumc'),(30,'2012-10-23 17:56:21',7,'1. There is no God<br>2. There is no hope','If this works, then',1,NULL,NULL,2,'Silliman University Medical Center',NULL,'09Mth','<iframe src=\"http://player.vimeo.com/video/784895\" width=\"500\" height=\"367\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe>','sumc'),(32,'2012-10-23 18:16:51',1,'Perhaps','Will JSoup let this video through?',1,NULL,NULL,2,'Baldwinternet and Gaming',NULL,'ehNBE','<iframe width=\"640\" height=\"480\" src=\"http://www.youtube.com/embed/y8Kyi0WNg40?rel=0\" frameborder=\"0\" allowfullscreen=\"\"></iframe>','baldwinternet'),(33,'2012-10-23 18:17:17',1,'Perhaps','Will JSoup let this hack through?',1,NULL,NULL,2,'Baldwinternet and Gaming',NULL,'ehNBE','','baldwinternet'),(34,'2012-10-23 18:18:26',1,'Perhaps','Will JSoup let this hack through?',1,NULL,NULL,2,'Baldwinternet and Gaming',NULL,'ehNBE','Please oh please let my script through','baldwinternet'),(35,'2012-10-23 18:24:19',2,'Hopefully','Will JSoup let this vimeo vid through?',0,NULL,NULL,2,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture','<iframe src=\"http://player.vimeo.com/video/784895\" width=\"500\" height=\"367\" frameborder=\"0\" allowfullscreen=\"\"></iframe> \n<p><a href=\"http://vimeo.com/784895\" rel=\"nofollow\">Dramatic Chipmunk</a> from <a href=\"http://vimeo.com/user401537\" rel=\"nofollow\">Julien Marguerit</a> on <a href=\"http://vimeo.com\" rel=\"nofollow\">Vimeo</a>.</p>','Mark Martinez'),(36,'2012-10-23 18:25:10',7,'Hopefully','Will JSoup let this vimeo vid through?',1,NULL,NULL,2,'Silliman University Medical Center',NULL,'09Mth','<iframe src=\"http://player.vimeo.com/video/784895\" width=\"500\" height=\"367\" frameborder=\"0\" allowfullscreen=\"\"></iframe> \n<p><a href=\"http://vimeo.com/784895\" rel=\"nofollow\">Dramatic Chipmunk</a> from <a href=\"http://vimeo.com/user401537\" rel=\"nofollow\">Julien Marguerit</a> on <a href=\"http://vimeo.com\" rel=\"nofollow\">Vimeo</a>.</p>','sumc'),(37,'2012-10-23 18:26:36',18,'The FSM will make sure this gets through.','What about a large image?',1,NULL,NULL,1,'Pins And Shit Dumaguete City','QKPln','gVBWJ',NULL,'pinsandshit'),(38,'2012-10-23 23:18:26',2,'Personally, I think it\'s stupid','Carissa likes this stupid video',0,NULL,NULL,2,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture','<iframe width=\"640\" height=\"480\" src=\"http://www.youtube.com/embed/N9iYkJGyj-w?rel=0\" frameborder=\"0\" allowfullscreen=\"\"></iframe>','Mark Martinez'),(39,'2012-10-24 16:48:23',2,'Phoned-in advertisement','Phoned-in category advertisements',0,3,'Phoned-in category',7,'Mark Martinez','6HO6i','http://graph.facebook.com/534053654/picture','sumc/3','Mark Martinez'),(40,'2012-10-24 16:49:01',7,'Phoned-in advertisement','Phoned-in category advertisements',1,3,'Phoned-in category',7,'Silliman University Medical Center','6HO6i','09Mth','sumc/3','sumc'),(41,'2012-10-24 18:13:30',2,'Negros Oriental (Cebuano: Sidlakang Negros), also called Oriental Negros or &quot;Eastern Negros&quot;, is a province of the Philippines in th Visayas','Check out the Negros Oriental tourism pins!',0,14,'Tourism Pins',7,'Mark Martinez','TVq2b','http://graph.facebook.com/534053654/picture','pinsandshit/14','Mark Martinez'),(42,'2012-10-24 18:14:48',18,'Negros Oriental (Cebuano: Sidlakang Negros), also called Oriental Negros or &quot;Eastern Negros&quot;, is a province of the Philippines\' Visayas reg.','Check out the Negros Oriental tourism pins!',1,14,'Tourism Pins',7,'Pins And Shit Dumaguete City','TVq2b','gVBWJ','pinsandshit/14','pinsandshit'),(43,'2012-10-24 21:03:48',18,'Even the crappiest school deserves a pin!<br>Except Maxino College. Jesus that school sucks ass','School Pins',1,13,'School Pins',6,'Pins And Shit Dumaguete City','Mig4K','gVBWJ','pinsandshit/13','pinsandshit'),(44,'2012-10-25 00:35:48',7,'what','what?',1,5,'pic whore',6,'Silliman University Medical Center',NULL,'09Mth','sumc/5','sumc'),(45,'2012-10-25 02:16:41',7,'Check out this shit','Check this shit out',1,NULL,NULL,3,'Silliman University Medical Center',NULL,'09Mth','http://4chan.org/b/','sumc'),(46,'2012-10-25 11:16:42',2,'I might have cancer','I... I just don\'t know',0,1,'SUMC Cancer Detection Services ',7,'Mark Martinez','Nl36w','http://graph.facebook.com/534053654/picture','sumc/1','Mark Martinez'),(47,'2012-10-25 11:30:59',7,'Whoever told you I was letting go?<br>The only joy that I have ever known<br>Girl is crying<br>Some people say<br>That everything has got its place and ','I wanna know',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(48,'2012-10-25 11:37:37',7,'time<br>Even the day must give way to the night<br>But I\'m not crying<br>Brr brr brr brr brr brr brr even if they cry<br>There is only one ways treet fo','for you and I, for you and Aiiii I never wann',1,NULL,NULL,1,'Silliman University Medical Center','5cMPA','09Mth',NULL,'sumc'),(49,'2012-10-25 11:43:44',2,'hello hello hello','hello',0,NULL,NULL,NULL,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(50,'2012-10-25 11:50:42',7,'123','abc',1,NULL,NULL,1,'Silliman University Medical Center','qPELw','09Mth',NULL,'sumc'),(51,'2012-10-25 11:52:37',2,'wake up in the morning feeling like der fuhrer','tik tok',0,NULL,NULL,1,'Mark Martinez','Gb9Nm','http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(52,'2012-10-25 11:53:08',2,'gonna make race purer','on the clock',0,NULL,NULL,1,'Mark Martinez','qjnn8','http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(53,'2012-10-25 11:53:54',2,'let me post','just',0,NULL,NULL,NULL,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(54,'2012-10-25 11:54:17',2,'a little touch','just',0,NULL,NULL,NULL,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(55,'2012-10-25 14:23:31',2,'pretty damn sad','how sa are you today?',0,1,'SUMC Cancer Detection Services ',7,'Mark Martinez','Nl36w','http://graph.facebook.com/534053654/picture','sumc/1','Mark Martinez'),(56,'2012-10-25 14:35:42',2,'ass','ass',0,NULL,NULL,NULL,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(57,'2012-10-25 15:35:33',4,'And this is <br>Jackass','Hi I\'m Jesus',0,NULL,NULL,1,'Jesus Christ','29vsy','http://graph.facebook.com/100004037722317/picture',NULL,'Jesus Christ'),(58,'2012-10-25 22:25:39',2,'that\'s the weirdest thing','i can unsubscribe from myself',0,NULL,NULL,1,'Mark Martinez','qv3fS','http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(59,'2012-10-27 23:09:26',23,'Go away!','We don\'t want none of y\'all niggers shopping here',1,NULL,NULL,NULL,'Lee Super Plaza',NULL,NULL,NULL,'leeplaza'),(60,'2012-10-28 00:28:46',2,'butt','butt',0,NULL,NULL,NULL,'Mark Martinez',NULL,'http://graph.facebook.com/534053654/picture',NULL,'Mark Martinez'),(61,'2012-10-29 10:36:35',8,'Yeah!','First Post!',1,NULL,NULL,NULL,'Religion',NULL,'z5vpn',NULL,'religion'),(62,'2012-10-29 11:46:49',7,'fasdfasdfsdf','dsfasdfasd',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(63,'2012-10-29 12:06:17',7,'sdfsdfsdfsdfsdf','hello',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(64,'2012-10-29 12:06:25',7,'adfadfasfadfasdfasdf','asdfasdfasdfasdf',1,NULL,NULL,NULL,'Silliman University Medical Center',NULL,'09Mth',NULL,'sumc'),(65,'2012-10-29 12:13:29',7,'asd<br>asd<br>asd<br>sad<br>as<br>das<br>das<br>da<br>sd','abc',1,NULL,NULL,1,'Silliman University Medical Center','guAUj','09Mth',NULL,'sumc'),(66,'2012-10-29 12:20:41',7,'What is this troll doing?','Don\'t feed him',1,7,'sumc',6,'Silliman University Medical Center','VEHBI','09Mth','baldwinternet4/7','sumc'),(67,'2012-11-04 02:55:00',10,'Well? Well?','something',0,NULL,NULL,NULL,'martinezdescent',NULL,'http://graph.facebook.com/1089289311/picture',NULL,'martinezdescent'),(68,'2012-11-04 02:55:25',10,'It works. It works...<br>I like that part','He said',0,NULL,NULL,NULL,'martinezdescent',NULL,'http://graph.facebook.com/1089289311/picture',NULL,'martinezdescent');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productPics`
--

DROP TABLE IF EXISTS `productPics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productPics` (
  `imageId` bigint(20) NOT NULL,
  `productId` bigint(20) NOT NULL,
  UNIQUE KEY `productId` (`productId`),
  KEY `FKA715B1B826EAE777` (`productId`),
  KEY `FKA715B1B85DE0B4F2` (`imageId`),
  CONSTRAINT `FKA715B1B826EAE777` FOREIGN KEY (`productId`) REFERENCES `imgurs` (`imageId`),
  CONSTRAINT `FKA715B1B85DE0B4F2` FOREIGN KEY (`imageId`) REFERENCES `products` (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productPics`
--

LOCK TABLES `productPics` WRITE;
/*!40000 ALTER TABLE `productPics` DISABLE KEYS */;
INSERT INTO `productPics` VALUES (1,306),(1,312),(1,313),(1,314),(1,315),(1,316),(1,317),(1,327),(3,143),(3,144),(4,145),(5,307),(5,331),(5,333),(5,334),(5,335),(5,336),(6,274),(6,275),(6,276),(9,319),(9,320),(9,321),(9,322),(9,323),(9,324),(9,325),(9,326),(10,377),(10,378),(12,346),(13,347),(13,348),(13,349),(13,350),(13,351),(13,352),(13,353),(13,354),(13,355),(14,357);
/*!40000 ALTER TABLE `productPics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `productId` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` longtext,
  `name` varchar(255) DEFAULT NULL,
  `categoryId` bigint(20) NOT NULL,
  `mainpicId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`productId`),
  KEY `FKC42BD1641D90ACEA` (`categoryId`),
  KEY `FKC42BD1645790B739` (`mainpicId`),
  CONSTRAINT `FKC42BD1641D90ACEA` FOREIGN KEY (`categoryId`) REFERENCES `categories` (`categoryId`),
  CONSTRAINT `FKC42BD1645790B739` FOREIGN KEY (`mainpicId`) REFERENCES `imgurs` (`imageId`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Goddamn phones','Phone',3,128),(2,'Malignant growths that will slowly kill you','Cancer',3,132),(3,'Colorectal cancer, commonly known as colon cancer or bowel cancer, is a cancer from uncontrolled cell growth in the colon or rectum (parts of the large intestine), or in the appendix. Genetic analysis shows that colon and rectal tumours are essentially genetically the same cancer. Symptoms of colorectal cancer typically include rectal bleeding and anemia which are sometimes associated with weight loss and changes in bowel habits.<br><br>Most colorectal cancer occurs due to lifestyle and increasing age with only a minority of cases associated with underlying genetic disorders. It typically starts in the lining of the bowel and if left untreated, can grow into the muscle layers underneath, and then through the bowel wall. Screening is effective at decreasing the chance of dying from colorectal cancer and is recommended starting at the age of 50 and continuing until a person is 75 years old. Localized bowel cancer is usually diagnosed through sigmoidoscopy or colonoscopy.<br><br>Cancers that are confined within the wall of the colon are often curable with surgery while cancer that has spread widely around the body is usually not curable and management then focuses on extending the person\'s life via chemotherapy and improving quality of life. Colorectal cancer is the third most commonly diagnosed cancer in the world, but it is more common in developed countries. Around 60% of cases were diagnosed in the developed world. It is estimated that worldwide, in 2008, 1.23 million new cases of colorectal cancer were clinically diagnosed, and that it killed 608,000 people.','Colorectal cancer',3,142),(4,'porno! Who doesn\'t love porno? maybe if you\'re queer','Porno',5,NULL),(5,'A pic-whore product, that will take any pic up its arse','pic whore',3,NULL),(6,'sacraments','Sacraments',6,NULL),(7,'same name as sumc trololol','sumc',11,308),(8,'cities and towns of neg. or','Tourism pins neg. or. ',12,NULL),(9,'butt','ass',10,318),(10,'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.<br><br>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.<br><br>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.<br><br>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.','Some product',3,337),(11,'pro-duct','Another product',3,338),(12,'Pins for the great City of Dumaguete','Dumaguete Pins',14,339),(13,'Pins for Schools','School Pins',14,340),(14,'Pins for the Municipality of Manjuyod','Manjuyod Pins',14,341),(15,'Pins for the Municipality of San Jose, Negros Oriental','San Jose Pins',14,342),(16,'Pins for the Municipality of Amlan, Negros Oriental','Amlan Pins',14,343),(17,'Pins for Movies and advocacies','Novelty pins',14,345),(18,'What?','Neil deGrasse Tyson',1,356);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `toptencandidates`
--

DROP TABLE IF EXISTS `toptencandidates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `toptencandidates` (
  `itemId` bigint(20) NOT NULL AUTO_INCREMENT,
  `link` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `votes` int(11) DEFAULT NULL,
  `creatorId` bigint(20) NOT NULL,
  `imgurId` bigint(20) DEFAULT NULL,
  `listId` bigint(20) NOT NULL,
  PRIMARY KEY (`itemId`),
  KEY `FKBFDEEBF850462CE5` (`creatorId`),
  KEY `FKBFDEEBF8D7ED73E8` (`imgurId`),
  KEY `FKBFDEEBF8CF379E12` (`listId`),
  CONSTRAINT `FKBFDEEBF850462CE5` FOREIGN KEY (`creatorId`) REFERENCES `UserConnection` (`connection_id`),
  CONSTRAINT `FKBFDEEBF8CF379E12` FOREIGN KEY (`listId`) REFERENCES `toptenlists` (`id`),
  CONSTRAINT `FKBFDEEBF8D7ED73E8` FOREIGN KEY (`imgurId`) REFERENCES `imgurs` (`imageId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toptencandidates`
--

LOCK TABLES `toptencandidates` WRITE;
/*!40000 ALTER TABLE `toptencandidates` DISABLE KEYS */;
INSERT INTO `toptencandidates` VALUES (1,NULL,'Mark\'s house',1,2,NULL,1);
/*!40000 ALTER TABLE `toptencandidates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `toptenlists`
--

DROP TABLE IF EXISTS `toptenlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `toptenlists` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `totalVotes` int(11) DEFAULT NULL,
  `creatorId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF41FC9ED50462CE5` (`creatorId`),
  CONSTRAINT `FKF41FC9ED50462CE5` FOREIGN KEY (`creatorId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toptenlists`
--

LOCK TABLES `toptenlists` WRITE;
/*!40000 ALTER TABLE `toptenlists` DISABLE KEYS */;
INSERT INTO `toptenlists` VALUES (1,'2012-10-31 13:29:46','Drug dens',1,2),(2,'2012-10-31 13:29:57','Beerhouses',0,2),(3,'2012-10-31 13:30:03','Hospitals',0,2),(4,'2012-10-31 13:30:15','Parking spaces',0,2),(5,'2012-10-31 13:30:24','Ugliest people',0,2),(6,'2012-10-31 13:35:26','High Schools',0,2),(7,'2012-10-31 13:35:34','Elementary Schools',0,2);
/*!40000 ALTER TABLE `toptenlists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `toptenvotes`
--

DROP TABLE IF EXISTS `toptenvotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `toptenvotes` (
  `userId` bigint(20) NOT NULL,
  `candidateId` bigint(20) NOT NULL,
  PRIMARY KEY (`userId`,`candidateId`),
  KEY `FKF4AF7121FCD9487C` (`candidateId`),
  KEY `FKF4AF7121602354E` (`userId`),
  CONSTRAINT `FKF4AF7121602354E` FOREIGN KEY (`userId`) REFERENCES `toptencandidates` (`itemId`),
  CONSTRAINT `FKF4AF7121FCD9487C` FOREIGN KEY (`candidateId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toptenvotes`
--

LOCK TABLES `toptenvotes` WRITE;
/*!40000 ALTER TABLE `toptenvotes` DISABLE KEYS */;
INSERT INTO `toptenvotes` VALUES (1,2);
/*!40000 ALTER TABLE `toptenvotes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userSubs`
--

DROP TABLE IF EXISTS `userSubs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userSubs` (
  `subscriberId` bigint(20) NOT NULL,
  `userId` bigint(20) DEFAULT NULL,
  KEY `FKF01D8C9E40215A21` (`subscriberId`),
  CONSTRAINT `FKF01D8C9E40215A21` FOREIGN KEY (`subscriberId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userSubs`
--

LOCK TABLES `userSubs` WRITE;
/*!40000 ALTER TABLE `userSubs` DISABLE KEYS */;
INSERT INTO `userSubs` VALUES (1,2),(3,2),(9,2),(1,10);
/*!40000 ALTER TABLE `userSubs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userextensions`
--

DROP TABLE IF EXISTS `userextensions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userextensions` (
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `userId` (`userId`),
  KEY `FK4A4D561FFAC22CC4` (`userId`),
  CONSTRAINT `FK4A4D561FFAC22CC4` FOREIGN KEY (`userId`) REFERENCES `UserConnection` (`connection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userextensions`
--

LOCK TABLES `userextensions` WRITE;
/*!40000 ALTER TABLE `userextensions` DISABLE KEYS */;
INSERT INTO `userextensions` VALUES (2),(10);
/*!40000 ALTER TABLE `userextensions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-06  1:49:02
