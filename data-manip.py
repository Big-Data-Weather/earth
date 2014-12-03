import json
from pprint import pprint
json_data=open('json_data.json')
datalist = []

data = json.load(json_data)
for i in data:
	data = i['data']
	for point in data:
		# change the zero and the operation to whatever you please
		datalist.append(point * 0)
	# pprint(datalist)

with open('json_data.json', 'r+') as f:
    data = json.load(f)
    
    for i in data:
		i['data'] = datalist
    f.seek(0)        # <--- should reset file position to the beginning.
    json.dump(data, f, indent=4)
    json.dump(data,json_data, 'data')

json_data.close()