package huce.it.datnbackend.services.brand;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BrandServiceImpl implements IBrandService{

    @Autowired
    private BrandRepository repository;

    @Override
    public List<BrandEntity> getAll() {
        return repository.findAllActive();
    }

    @Override
    public BrandEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(BrandEntity brandEntity) {
        repository.save(brandEntity);
        return 0;
    }

    @Override
    public int updateObject(BrandEntity brandEntity) {
        repository.save(brandEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        BrandEntity brandEntity = repository.findById(id).get();
        brandEntity.setStatus(0);
        repository.save(brandEntity);
        return 200;
    }
}
