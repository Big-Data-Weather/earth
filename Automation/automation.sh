#!/bin/bash


day1=$(date -d "1 day ago" '+%Y%m%d')
echo "downloading for ${day1}"
curl "http://nomads.ncep.noaa.gov/cgi-bin/filter_gfs_1p00.pl?file=gfs.t00z.pgrb2.1p00.f000&lev_10_m_above_ground=on&var_UGRD=on&var_VGRD=on&leftlon=0&rightlon=360&toplat=90&bottomlat=-90&dir=%2Fgfs.${day1}00"  -o day1.grib2

day2=$(date -d "8 day ago" '+%Y%m%d')
echo "downloading for ${day2}"
curl "http://nomads.ncep.noaa.gov/cgi-bin/filter_gfs_1p00.pl?file=gfs.t00z.pgrb2.1p00.f000&lev_10_m_above_ground=on&var_UGRD=on&var_VGRD=on&leftlon=0&rightlon=360&toplat=90&bottomlat=-90&dir=%2Fgfs.${day2}00"  -o day2.grib2

echo "begin filtering"

grib2jsonsaved/grib2json-0.8.0-SNAPSHOT/bin/grib2json -c --names --data --fp 2 --fs 103 --fv 10.0 -o  u-grd1.json day1.grib2
grib2jsonsaved/grib2json-0.8.0-SNAPSHOT/bin/grib2json -c --names --data --fp 3 --fs 103 --fv 10.0 -o  v-grd1.json day1.grib2

grib2jsonsaved/grib2json-0.8.0-SNAPSHOT/bin/grib2json -c --names --data --fp 2 --fs 103 --fv 10.0 -o u-grd2.json day2.grib2
grib2jsonsaved/grib2json-0.8.0-SNAPSHOT/bin/grib2json -c --names --data --fp 3 --fs 103 --fv 10.0 -o v-grd2.json day2.grib2

echo "begin calculating difference"

java -cp .:json-20090211.jar DifferenceCreator

echo "begin moving files"

rm /srv/www/myappnamehere/current/public/data/weather/current/current-temp-surface-level-gfs-1.0.json
rm /srv/www/myappnamehere/current/public/data/weather/current/current-wind_actual-surface-level-gfs-1.0.json
rm /srv/www/myappnamehere/current/public/data/weather/current/current-wind_model-surface-level-gfs-1.0.json

cp -n current-temp-surface-level-gfs-1.0.json /srv/www/myappnamehere/current/public/data/weather/current/
cp -n current-wind_actual-surface-level-gfs-1.0.json /srv/www/myappnamehere/current/public/data/weather/current/
cp -n current-wind_model-surface-level-gfs-1.0.json /srv/www/myappnamehere/current/public/data/weather/current/

echo "done"
