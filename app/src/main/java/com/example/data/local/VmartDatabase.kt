package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        WishlistItemEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VmartDatabase : RoomDatabase() {

    abstract fun vmartDao(): VmartDao

    companion object {
        @Volatile
        private var INSTANCE: VmartDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): VmartDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VmartDatabase::class.java,
                    "vmart_ecommerce.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate initial products and orders
                            scope.launch(Dispatchers.IO) {
                                val dao = INSTANCE?.vmartDao()
                                dao?.insertProducts(InitialData.sampleProducts)
                                for (order in InitialData.sampleOrders) {
                                    dao?.insertOrder(order)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
