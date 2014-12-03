import json
from pprint import pprint
from json import encoder

json_data=open('current-wind_actual-surface-level-gfs-1.0.json')
datalist = []
encoder.FLOAT_REPR = lambda o: format(o, '.2f')

data = json.load(json_data)
for i in data:
	data = i['data']

print (len(data))
for point in data:
	# change the zero and the operation to whatever you please
	datalist.append(point * 2)
# pprint(datalist)

with open('current-wind_model-surface-level-gfs-1.0.json', 'r+') as f:
    data = json.load(f)
    
    for i in data:
		i['data'] = datalist
    f.seek(0)        # <--- should reset file position to the beginning.
    json.dump(data, f, separators = (',', ':'))
    #json.dump(data,json_data, 'data', separators = (',', ':'))

json_data.close()