import json
from pprint import pprint
from json import encoder

json_data=open('current-wind_model-surface-level-gfs-1.0.json')
json_data2=open('current-wind_actual-surface-level-gfs-1.0.json')
datalist = []
encoder.FLOAT_REPR = lambda o: format(o, '.2f')

data = json.load(json_data)
data2 = json.load(json_data2)
for i in data:
	data = i['data']
for i in data2:
	data2 = i['data']

print (len(data), len(data2))
for point in range(0,len(data)):
	# model minus actual
	#print (point)
	datalist.append(data[point] - data2[point])
	# print(data[point], data2[point])
# pprint(datalist)

with open('current-wind_difference-surface-level-gfs-1.0.json', 'r+') as f:
    data = json.load(f)
    
    for i in data:
		i['data'] = datalist
    f.seek(0)        # <--- should reset file position to the beginning.
    json.dump(data, f, separators = (',', ':'))
    json.dump(data,json_data, 'data', separators = (',', ':'))

json_data.close()